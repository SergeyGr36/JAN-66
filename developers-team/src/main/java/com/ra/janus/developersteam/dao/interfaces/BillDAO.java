package com.ra.janus.developersteam.dao.interfaces;

import com.ra.janus.developersteam.entity.Bill;

import java.util.List;

public interface BillDAO {
    List<Bill> readAll();

    Bill read(long id);

    boolean update(Bill entity);

    boolean delete(long id);

    Bill create(Bill entity);
}
