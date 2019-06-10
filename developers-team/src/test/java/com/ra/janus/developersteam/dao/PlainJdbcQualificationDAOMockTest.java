package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Qualification;
import java.util.HashMap;
import java.util.Map;

class PlainJdbcQualificationDAOMockTest extends BaseDAOMockTest {

    private BaseDao<Qualification> qualificationDAO;
    private Qualification qualification;

    @Override
    protected BaseEntity getTestEntity() {
        if (qualification == null)
            qualification = new Qualification(testId, "Web Developer", "Front End");
        return qualification;
    }
    @Override
    protected BaseDao getDAO() {
        qualificationDAO = new PlainJdbcQualificationDAO(mockTemplate);
        return qualificationDAO;
    }
    @Override
    protected String getInsertSql() {
        return "INSERT INTO qualifications (name, responsibility) VALUES (?, ?)";
    }
    @Override
    protected String getUpdateSql() {
        return "UPDATE qualifications SET name=?,responsibility=? WHERE id=?";
    }
    @Override
    protected String getSelectAllSql() {
        return "SELECT * FROM qualifications";
    }
    @Override
    protected String getSelectOneSql() {
        return "SELECT * FROM qualifications WHERE id = ?";
    }
    @Override
    protected String getDeleteSql() {
        return "DELETE FROM qualifications WHERE id=?";
    }
    @Override
    protected Map<String, Object> getTestEntityMap() {
        Map<String, Object> testMap = new HashMap<>(1);
        Qualification qualification = (Qualification) getTestEntity();
        testMap.put("id", qualification.getId());
        testMap.put("name", qualification.getName());
        testMap.put("responsibility", qualification.getResponsibility());
        return testMap;
    }
}