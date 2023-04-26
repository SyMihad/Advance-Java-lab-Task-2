package ecom.repository;

import ecom.domain.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerRepository {
    private SessionFactory sessionFactory;

    public CustomerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Customer> list() {
        Session session = sessionFactory.getCurrentSession();
//        TypedQuery<Customer> departmentQuery = session.createQuery("from Customer", Customer.class);
//        return departmentQuery.getResultList();
//        String sql = "SELECT u from Customer u LEFT JOIN FETCH u.addresss o";
//        Query<Customer> query = session.createQuery(sql, Customer.class);
//        List<Customer> users = query.getResultList();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> customerRoot = query.from(Customer.class);
        customerRoot.fetch("addresses", JoinType.LEFT);
        query.select(customerRoot).distinct(true);
        return session.createQuery(query).getResultList();
    }

    public boolean create(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.save(customer);
        return true;
    }

    public Customer get(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Customer.class, id);
    }

    public boolean update(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.update(customer);
        return true;
    }

    public boolean delete(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(customer);
        return true;
    }
}
