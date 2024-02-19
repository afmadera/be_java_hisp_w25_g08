package com.grupo08.socialmeli.repository;

import com.grupo08.socialmeli.entity.Buyer;
import com.grupo08.socialmeli.entity.Seller;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class BuyerRepositoryImpl implements IBuyerRepository {
    private final List<Buyer> listBuyers = new ArrayList<>(Arrays.asList(
        new Buyer(1, "Fabian", new ArrayList<>(Arrays.asList(
                new Seller(1,"Seller1",null,null),
                new Seller(2,"Seller2",null,null)
        ))),
        new Buyer(2, "Miguel", new ArrayList<>()),
        new Buyer(3, "Andres", new ArrayList<>())
    ));


    public BuyerRepositoryImpl() {
    }

    @Override
    public List<Buyer> findAll() {
        return listBuyers;
    }

    @Override
    public Optional<Buyer> findById(int id) {
        return listBuyers.stream().filter(buyer -> buyer.getId() == id).findFirst();
    }
}
