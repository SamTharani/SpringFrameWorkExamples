package com.spring.mvc.dao;

import com.spring.mvc.model.Contact;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ContactDAOImpl extends JdbcDaoSupport implements ContactDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveOrUpdate(Contact contact) {

        if (contact.getContactId() > 0) {
            String sql = "UPDATE contact SET name=?, email=?, address=?, telephone=? WHERE contact_id=?";
            jdbcTemplate.update(sql, contact.getName(), contact.getEmail(), contact.getAddress(),
                    contact.getTelephone(), contact.getContactId());
        } else {
            String sql = "INSERT INTO contact (name, email, address, telephone) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, contact.getName(), contact.getEmail(), contact.getAddress(), contact.getTelephone());
        }

    }

    @Override
    public void delete(int contactId) {
        String sql = "DELETE FROM contact WHERE contact_id=?";
        jdbcTemplate.update(sql, contactId);
    }

    @Override
    public Contact get(int contactId) {

        String sql = "SELECT * FROM contact WHERE contact_id=" + contactId;
        return jdbcTemplate.query(sql, new ResultSetExtractor<Contact>() {

            @Override
            public Contact extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next()) {
                    Contact contact = new Contact();
                    contact.setContactId(resultSet.getInt("contact_id"));
                    contact.setName(resultSet.getString("name"));
                    contact.setEmail(resultSet.getString("email"));
                    contact.setAddress(resultSet.getString("address"));
                    contact.setTelephone(resultSet.getString("telephone"));
                    return contact;
                }
                return null;
            }
        });


    }

    @Override
    public List<Contact> list() throws DataAccessException {

        String sql = "SELECT * FROM contact";

        List<Contact> contacts = new ArrayList<Contact>();

        List<Map<String, Object>> contactRows = getJdbcTemplate().queryForList(sql);

        for (Map row : contactRows) {

            Contact contact = new Contact();
            contact.setContactId((Integer) (row.get("contact_id")));
            contact.setName((String) row.get("name"));
            contact.setEmail((String) row.get("email"));
            contact.setAddress((String) row.get("address"));
            contact.setTelephone((String) row.get("telephone"));
            contacts.add(contact);
        }

        return contacts;

    }

}
