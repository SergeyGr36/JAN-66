package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;

public interface Delegate {
    BaseEntity execute(BaseEntity entity);
}
