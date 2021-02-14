package org.mai.dep810.collections_lesson.auction;

import java.math.BigDecimal;
import java.util.*;


public class AuctionImpl implements Auction {

    Map<String, Map<String, BigDecimal>> _product_map;

    public AuctionImpl() {
        _product_map = new HashMap<>();
    }

    @Override
    public void placeProduct(String product, BigDecimal initialPrice) {
        _product_map.put(product, new HashMap<>());
        _product_map.get(product).put(null, initialPrice);
    }

    @Override
    public void addBid(String user, String product, BigDecimal price) {
        if(!_product_map.containsKey(product))
            throw new ProductNotFoundException("Item not found");
        if(!(price.compareTo(getProductPrice(product)) > 0))
            throw new RuntimeException("Bid must overbid prev-s one");
        _product_map.get(product).put(user, price);
    }

    @Override
    public void removeBid(String user, String product) {
        if(!_product_map.containsKey(product))
            throw new ProductNotFoundException("Item not found");
        Map<String, BigDecimal> prod_data = _product_map.get(product);
        if(!prod_data.containsKey(user))
            throw new RuntimeException("User not found");
        prod_data.remove(user);
    }

    @Override
    public boolean sellProduct(String product) {
        if(!_product_map.containsKey(product))
            throw new ProductNotFoundException("Item not found");
        boolean selling = false;
        if(_product_map.get(product).size() > 1) {
            selling = true;
            _product_map.remove(product);
        }
        return selling;
    }

    @Override
    public String sellProductTo(String product) {
        if(!_product_map.containsKey(product))
            throw new ProductNotFoundException("Item not found");
        BigDecimal benefitial_price = getProductPrice(product);
        String buyer = null;
        if(_product_map.get(product).size() > 1) {
            for (Map.Entry<String, BigDecimal> row : _product_map.get(product).entrySet()) {
                if (row.getValue().equals(benefitial_price)) {
                    buyer = row.getKey();
                    break;
                }
            }
            _product_map.remove(product);
        }
        return buyer;
    }

    @Override
    public List<String> getProducts() {
        return new ArrayList<>(_product_map.keySet());
    }

    @Override
    public BigDecimal getProductPrice(String product) {
        if(!_product_map.containsKey(product))
            throw new ProductNotFoundException("Item not found");
        return Collections.max(_product_map.get(product).values());
    }

}
