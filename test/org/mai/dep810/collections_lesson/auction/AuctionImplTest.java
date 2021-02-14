package org.mai.dep810.collections_lesson.auction;

import org.junit.*;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class AuctionImplTest {

    Auction auc;
    BigDecimal default_price = new BigDecimal(25);
    String book3rdEdition = "Thinking Java 3rd edition";
    String book4thEdition = "Thinking Java 4th edition";

    /*Вызывается при инициализации класса AuctionImplTest*/
//    @BeforeClass
//    public static void setupClass(){
//
//    }

    /*Вызывается перед вызовом каждого метода помеченного аннотацией @Test*/
    @Before
    public void setUp() throws Exception {
        auc = new AuctionImpl();
        auc.placeProduct(book3rdEdition, default_price);
        auc.placeProduct(book4thEdition, default_price);
    }

    /*Вызывается после вызова каждого метода помеченного аннотацией @Test*/
    @After
    public void tearDown() throws Exception {
        auc = null;
    }

    /*Вызывается после вызова всех тестовых методов*/
//    @AfterClass
//    public static void releaseRecources() {
//
//    }

    @Test
    public void placeProduct() {
        List<String> products = auc.getProducts();
        assertThat(products, hasItems(book3rdEdition, book4thEdition));

        String book5thEdition = "Thinking Java 5th edition";
        assertThat(products, not(hasItem(book5thEdition)));
        auc.placeProduct(book5thEdition, default_price);

        products = auc.getProducts();
        assertThat(products, hasItem(book5thEdition));
    }

    @Test
    public void addBid() {
        auc.addBid("Senya", book3rdEdition, new BigDecimal(30));
        try {
            auc.addBid("Danila", book4thEdition, new BigDecimal(25));
            auc.addBid("Vova", book3rdEdition, new BigDecimal(28));
        }
        catch (RuntimeException ex) {
            assertEquals("Bid must overbid prev-s one", ex.getMessage());
        }

        String book5thEdition = "Thinking Java 5th edition";
        try {
            auc.addBid("Dimon", book5thEdition, new BigDecimal(30));
        }
        catch(ProductNotFoundException ex) {
            assertEquals("Item not found", ex.getMessage());
        }

        auc.placeProduct(book5thEdition, default_price);
        auc.addBid("Dimon", book5thEdition, new BigDecimal(33));
        assertEquals(new BigDecimal(33), auc.getProductPrice(book5thEdition));
    }

    @Test
    public void removeBid() {
        auc.addBid("Senya", book3rdEdition, new BigDecimal(30));
        auc.addBid("Danila", book3rdEdition, new BigDecimal(35));
        auc.addBid("Dimon", book3rdEdition, new BigDecimal(40));
        auc.removeBid("Danila", book3rdEdition);
        auc.removeBid("Dimon", book3rdEdition);
        try {
            auc.removeBid("Anton", book3rdEdition);
        }
        catch (RuntimeException ex) {
            assertEquals("User not found", ex.getMessage());
        }
        String book5thEdition = "Thinking Java 5th edition";
        try {
            auc.removeBid("Dimon", book5thEdition);
        }
        catch (ProductNotFoundException ex) {
            assertEquals("Item not found", ex.getMessage());
        }
        assertEquals(new BigDecimal(30), auc.getProductPrice(book3rdEdition));
    }

    @Test
    public void sellProduct() {
        assertFalse(auc.sellProduct(book3rdEdition));
        assertThat(auc.getProducts(), hasItem(book3rdEdition));

        auc.addBid("Vasya", book4thEdition, new BigDecimal(30));
        assertTrue(auc.sellProduct(book4thEdition));
        try {
            assertTrue(auc.sellProduct("book5thEdition"));
        }
        catch (ProductNotFoundException ex) {
            assertEquals("Item not found", ex.getMessage());
        }
        assertThat(auc.getProducts(), not(hasItem(book4thEdition)));
    }

    @Test
    public void sellProductTo() {
        assertNull(auc.sellProductTo(book3rdEdition));
        try {
            assertNull(auc.sellProductTo("book5thEdition"));
        }
        catch (ProductNotFoundException ex) {
            assertEquals("Item not found", ex.getMessage());
        }
        auc.addBid("Senya", book4thEdition, new BigDecimal(30));
        auc.addBid("Vasya", book4thEdition, new BigDecimal(45));
        auc.addBid("Dimon", book4thEdition, new BigDecimal(49));
        assertEquals("Dimon", auc.sellProductTo(book4thEdition));
    }
}