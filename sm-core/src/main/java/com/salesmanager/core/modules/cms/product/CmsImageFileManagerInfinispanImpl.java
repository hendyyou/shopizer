package com.salesmanager.core.modules.cms.product;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.content.model.content.FileContentType;
import com.salesmanager.core.business.content.model.content.ImageContentFile;
import com.salesmanager.core.business.content.model.content.InputContentFile;
import com.salesmanager.core.business.content.model.content.OutputContentFile;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.modules.cms.common.CacheAttribute;
import com.salesmanager.core.modules.cms.impl.CacheManager;

/**
 * Manager for storing in retrieving image files from the CMS This is a layer on top of Infinispan
 * https://docs.jboss.org/author/display/ISPN/Tree+API+Module
 * 
 * @author csamson
 */
public class CmsImageFileManagerInfinispanImpl
    implements ProductImagePut, ProductImageGet, ProductImageRemove
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CmsImageFileManagerInfinispanImpl.class );

    private static CmsImageFileManagerInfinispanImpl fileManager = null;

    private CacheManager cacheManager;

    /**
     * Requires to stop the engine when image servlet un-deploys
     */
    public void stopFileManager()
    {

        try
        {
        	cacheManager.getManager().stop();
            LOGGER.info( "Stopping CMS" );
        }
        catch ( Exception e )
        {
            LOGGER.error( "Error while stopping CmsImageFileManager", e );
        }
    }

    public static CmsImageFileManagerInfinispanImpl getInstance()
    {

        if ( fileManager == null )
        {
            fileManager = new CmsImageFileManagerInfinispanImpl();
        }

        return fileManager;

    }

    private CmsImageFileManagerInfinispanImpl()
    {

    }

    /**
     * root -productFiles -merchant-id PRODUCT-ID(key) -> CacheAttribute(value) - image 1 - image 2 - image 3
     */

    @Override
    public void addProductImage( ProductImage productImage,
    		InputContentFile contentImage )
        throws ServiceException
    {

        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }

        try
        {

 
            // product key
            String productPath = String.valueOf( productImage.getProduct().getSku() );

            Node<String, Object> merchantNode = getMerchantNode(productImage.getProduct().getMerchantStore().getCode());



            // object for a given product containing all images
            CacheAttribute productAttribute = (CacheAttribute) merchantNode.get( productPath );

            if ( productAttribute == null )
            {
                productAttribute = new CacheAttribute();
                productAttribute.setEntityId( productPath );
            }
            
            
            File file1 = new File( "c:/doc/carl/Merchant.jpg" );
            if ( !file1.exists() || !file1.canRead() )
            {
                throw new ServiceException( "Can't read" + file1.getAbsolutePath() );
            }

            
            InputStream isFile = contentImage.getFile();
            
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( isFile, output );

            
            productAttribute.getEntities().put( contentImage.getFileName(), output.toByteArray() );

            merchantNode.put( productPath, productAttribute );

        }
        catch ( Exception e )
        {

            throw new ServiceException( e );

        }

    }

    @Override
    public OutputContentFile getProductImage( ProductImage productImage )
        throws ServiceException
    {

       return getProductImage(productImage.getProduct().getMerchantStore().getCode(),productImage.getProduct().getSku(),productImage.getProductImage());

    }


    public List<OutputContentFile> getImages( MerchantStore store, FileContentType imageContentType )
        throws ServiceException
    {

         return getImages(store.getCode(),imageContentType);

    }

    @Override
    public List<OutputContentFile> getImages( Product product )
        throws ServiceException
    {

        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }

        List<OutputContentFile> images = new ArrayList<OutputContentFile>();
        FileNameMap fileNameMap = URLConnection.getFileNameMap();

        try
        {


            Node<String, Object> merchantNode = getMerchantNode(product.getMerchantStore().getCode());

            if ( merchantNode == null )
            {
                return null;
            }

            CacheAttribute productAttribute = (CacheAttribute) merchantNode.get( String.valueOf( product.getSku() ) );

            if ( productAttribute == null )
            {
                return null;
            }

            Map<String, byte[]> imgs = productAttribute.getEntities();
            Set<String> imageNames = imgs.keySet();
            for ( String imageName : imageNames )
            {

                byte[] imageBytes = imgs.get( imageName );

                OutputContentFile contentImage = new OutputContentFile();

                InputStream input = new ByteArrayInputStream( imageBytes );
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                IOUtils.copy( input, output );

                String contentType = fileNameMap.getContentTypeFor( imageName );

                contentImage.setFile( output );
                contentImage.setMimeType( contentType );
                contentImage.setFileName( imageName );

                images.add( contentImage );

            }

        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {

        }

        return images;
    }



	@Override
    public void removeImages( final String merchantStoreCode )
        throws ServiceException
    {
        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }

        try
        {


			final StringBuilder merchantPath = new StringBuilder();
	        merchantPath.append( "product-merchant-" ).append(merchantStoreCode );
	        cacheManager.getTreeCache().getRoot().remove(merchantPath.toString());
			


        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {

        }

    }


    @Override
    public void removeProductImage( ProductImage productImage )
        throws ServiceException
    {

        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }

        try
        {

            String productPath = String.valueOf( productImage.getProduct().getSku() );

            Node<String, Object> merchantNode = getMerchantNode(productImage.getProduct().getMerchantStore().getCode());

            if ( merchantNode == null )
            {
                return;
            }

            CacheAttribute productAttribute = (CacheAttribute) merchantNode.get( productPath );

            if ( productAttribute == null )
            {
                return;
            }
            
            productAttribute.getEntities().remove(productImage.getProductImage());
            
            

        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {

        }

    }

    @Override
    public void removeProductImages( Product product )
        throws ServiceException
    {

        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }

        try
        {

        	String productPath = String.valueOf( product.getSku());
        	
            Node<String, Object> merchantNode = getMerchantNode(product.getMerchantStore().getCode());

            if ( merchantNode == null )
            {
                return;
            }
            
            merchantNode.remove(productPath);
            

        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {

        }

    }


    @Override
	public List<OutputContentFile> getImages(final String merchantStoreCode,
			FileContentType imageContentType) throws ServiceException {
        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }
        List<OutputContentFile> images = new ArrayList<OutputContentFile>();
        FileNameMap fileNameMap = URLConnection.getFileNameMap();

        try
        {


            Node<String, Object> merchantNode = getMerchantNode(merchantStoreCode);

            if ( merchantNode == null )
            {
                return null;
            }

            Set<String> keys = merchantNode.getKeys();
            for ( String key : keys )
            {
                // Product
                CacheAttribute productAttribute = (CacheAttribute) merchantNode.get( key );

                Map<String, byte[]> imgs = productAttribute.getEntities();
                Set<String> imageNames = imgs.keySet();
                for ( String imageName : imageNames )
                {

                    byte[] imageBytes = imgs.get( imageName );

                    OutputContentFile contentImage = new OutputContentFile();

                    InputStream input = new ByteArrayInputStream( imageBytes );
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    IOUtils.copy( input, output );

                    String contentType = fileNameMap.getContentTypeFor( imageName );

                    contentImage.setFile( output );
                    contentImage.setMimeType( contentType );
                    contentImage.setFileName( imageName );

                    images.add( contentImage );

                }

            }


        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {

        }

        return images;
	}

	@Override
	public OutputContentFile getProductImage(String merchantStoreCode,
			String productCode, String imageName) throws ServiceException {
        if ( cacheManager.getTreeCache() == null )
        {
            throw new ServiceException( "CmsImageFileManagerInfinispan has a null cacheManager.getTreeCache()" );
        }
        InputStream input = null;
        OutputContentFile contentImage = new OutputContentFile();
        try
        {
        	String productPath = String.valueOf( productCode );

            Node<String, Object> merchantNode = getMerchantNode(merchantStoreCode);

            if ( merchantNode == null )
            {
                return null;
            }

            CacheAttribute productAttribute = (CacheAttribute) merchantNode.get( productPath );

            if ( productAttribute == null )
            {
                return null;
            }

            byte[] imageBytes = (byte[]) productAttribute.getEntities().get( imageName );

            if ( imageBytes == null )
            {
                return null;
            }

            input = new ByteArrayInputStream( imageBytes );
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy( input, output );

            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            String contentType = fileNameMap.getContentTypeFor( imageName );

            contentImage.setFile( output );
            contentImage.setMimeType( contentType );
            contentImage.setFileName( imageName );

        }
        catch ( Exception e )
        {
            throw new ServiceException( e );
        }
        finally
        {
            if ( input != null )
            {
                try
                {
                    input.close();
                }
                catch ( Exception ignore )
                {
                }
            }
        }

        return contentImage;
	}
	
	
	private Node<String, Object> getMerchantNode( final String storeCode )
    {
        LOGGER.debug( "Fetching merchant node for store {} from Infinispan", storeCode );
        final StringBuilder merchantPath = new StringBuilder();
        merchantPath.append( "product-merchant-" ).append(storeCode );

        Fqn contentFilesFqn = Fqn.fromString(merchantPath.toString()); 

		Node<String,Object> merchant = cacheManager.getTreeCache().getRoot().getChild(contentFilesFqn); 
        
        if(merchant==null) {

            cacheManager.getTreeCache().getRoot().addChild(contentFilesFqn);
            merchant = cacheManager.getTreeCache().getRoot().getChild(contentFilesFqn); 

        }
        
        return merchant;

    }

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

}
