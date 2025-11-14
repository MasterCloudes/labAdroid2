package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myapplication.DAO.CatDAO;
import com.example.myapplication.DAO.ProductDAO;
import com.example.myapplication.DTO.CatDTO;
import com.example.myapplication.DTO.ProductDTO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * Instrumented test, which will execute on an Android device.
 * This test covers all CRUD operations for CatDAO and ProductDAO.
 */
@RunWith(AndroidJUnit4.class)
public class FullCrudTest {

    private CatDAO catDAO;
    private ProductDAO productDAO;
    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Clear database before each test to ensure a clean state
        context.deleteDatabase("my_database.db");
        catDAO = new CatDAO(context);
        productDAO = new ProductDAO(context);
    }

    @After
    public void tearDown() {
        // You can add cleanup code here if needed, but setUp already clears the DB
    }

    @Test
    public void testCategoryCRUD() {
        // 1. CREATE a new category
        CatDTO newCat = new CatDTO();
        newCat.setName("Test Category");
        int newCatId = catDAO.addCat(newCat);
        assertTrue("Failed to create category, ID should be > 0", newCatId > 0);
        newCat.setId(newCatId);

        // 2. READ the category back
        CatDTO retrievedCat = catDAO.getCatById(newCatId);
        assertNotNull("Retrieved category should not be null", retrievedCat);
        assertEquals("Category name does not match", "Test Category", retrievedCat.getName());

        // 3. UPDATE the category
        retrievedCat.setName("Updated Test Category");
        boolean isUpdated = catDAO.updateRow(retrievedCat);
        assertTrue("Failed to update category", isUpdated);

        // Verify the update
        CatDTO updatedCat = catDAO.getCatById(newCatId);
        assertEquals("Category name was not updated correctly", "Updated Test Category", updatedCat.getName());

        // 4. DELETE the category
        boolean isDeleted = catDAO.deleteRow(newCatId);
        assertTrue("Failed to delete category", isDeleted);

        // Verify deletion
        CatDTO deletedCat = catDAO.getCatById(newCatId);
        assertNull("Category should be null after deletion", deletedCat);
    }

    @Test
    public void testProductCRUD_WithCategoryIntegration() {
        // --- Setup: Create a category first ---
        CatDTO parentCat = new CatDTO();
        parentCat.setName("Parent Category for Product");
        int parentCatId = catDAO.addCat(parentCat);
        assertTrue("Setup failed: Could not create parent category", parentCatId > 0);

        // 1. CREATE a new product
        ProductDTO newProduct = new ProductDTO();
        newProduct.setName("Test Product");
        newProduct.setPrice(123.45);
        newProduct.setId_cat(parentCatId);
        long newProductId = productDAO.addProduct(newProduct);
        assertTrue("Failed to create product, ID should be > 0", newProductId > 0);

        // 2. READ the product back
        ArrayList<ProductDTO> allProducts = productDAO.getAllProducts();
        ProductDTO retrievedProduct = null;
        for (ProductDTO p : allProducts) {
            if (p.getId() == newProductId) {
                retrievedProduct = p;
                break;
            }
        }
        assertNotNull("Retrieved product should not be null", retrievedProduct);
        assertEquals("Product name does not match", "Test Product", retrievedProduct.getName());
        assertEquals("Product price does not match", 123.45, retrievedProduct.getPrice(), 0.001);
        assertEquals("Product category ID does not match", parentCatId, retrievedProduct.getId_cat());

        // 3. UPDATE the product
        retrievedProduct.setName("Updated Test Product");
        retrievedProduct.setPrice(543.21);
        int updatedRows = productDAO.updateProduct(retrievedProduct);
        assertTrue("Failed to update product", updatedRows > 0);

        // Verify the update
        ArrayList<ProductDTO> updatedProducts = productDAO.getAllProducts();
        ProductDTO updatedProduct = null;
        for (ProductDTO p : updatedProducts) {
            if (p.getId() == newProductId) {
                updatedProduct = p;
                break;
            }
        }
        assertNotNull(updatedProduct);
        assertEquals("Product name was not updated correctly", "Updated Test Product", updatedProduct.getName());
        assertEquals("Product price was not updated correctly", 543.21, updatedProduct.getPrice(), 0.001);

        // 4. DELETE the product
        int deletedRows = productDAO.deleteProduct((int) newProductId);
        assertTrue("Failed to delete product", deletedRows > 0);

        // Verify deletion
        ArrayList<ProductDTO> remainingProducts = productDAO.getAllProducts();
        ProductDTO deletedProduct = null;
        for (ProductDTO p : remainingProducts) {
            if (p.getId() == newProductId) {
                deletedProduct = p;
                break;
            }
        }
        assertNull("Product should be null after deletion", deletedProduct);

        // --- Teardown: Clean up the parent category ---
        catDAO.deleteRow(parentCatId);
    }
}
