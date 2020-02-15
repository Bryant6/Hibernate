package test;

import bean.Category;
import bean.Product;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import java.util.*;

public class TestHibernate {
    public static void main(String[] args) {

        Map products = new HashMap();
        products.put("basketball",238f);
        products.put("clothe",100f);
        products.put("phone",2380f);
        products.put("iphone",10000f);
        products.put("ipad",2380f);
        products.put("food",68f);
        products.put("drink",23f);
        products.put("book",100f);
        products.put("Curry7",2380f);
        products.put("Bryant",1000f);
        Set s = products.entrySet();
        Iterator iter = s.iterator();

        SessionFactory sf = new Configuration().configure().buildSessionFactory();

        Session session = sf.openSession();
        session.beginTransaction();

        while(iter.hasNext()){
            Map.Entry entey = (Map.Entry) iter.next();
            Product product = new Product();        //瞬时状态
            product.setName((String) entey.getKey());
            product.setPrice((Float) entey.getValue());

            session.save(product);                  //持久状态
        }

        session.getTransaction().commit();

        session.close();                            //脱管状态
        sf.close();
    }

    @Test
    public void selectOne(){
        SessionFactory sf = new Configuration().configure().buildSessionFactory();

        Session session = sf.openSession();
        session.beginTransaction();

        Product product = (Product) session.get(Product.class,6);

        System.out.println(product);

        session.getTransaction().commit();

        session.close();
        sf.close();
    }

    @Test
    public void deleteOne(){
        SessionFactory sf = new Configuration().configure().buildSessionFactory();

        Session session = sf.openSession();
        session.beginTransaction();

        Product product = (Product) session.get(Product.class,11);

        session.delete(product);

        session.getTransaction().commit();

        session.close();
        sf.close();
    }

    @Test
    public void updataOne(){
        SessionFactory sf = new Configuration().configure().buildSessionFactory();

        Session session = sf.openSession();
        session.beginTransaction();

        Product product = (Product) session.get(Product.class,1);

        String name = product.getName();
        product.setName(name + "-update");
        session.update(product);

        session.getTransaction().commit();

        session.close();
        sf.close();
    }

    @Test
    public void selectByName(){
        SessionFactory sf = new Configuration().configure().buildSessionFactory();

        Session session = sf.openSession();
        session.beginTransaction();

        String name = "p";

//        Query query = session.createQuery("from Product p where p.name like ?");
//
//        query.setString(0,"%" + name + "%");
//        List<Product> products = query.list();

        Criteria c = session.createCriteria(Product.class);
        c.add(Restrictions.like("name","%"+name+"%"));

        List<Product> products = c.list();

        for(Product p:products){
            System.out.println(p);
        }

        session.getTransaction().commit();

        session.close();
        sf.close();
    }

    @Test
    public void manyToOne(){
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();

        Category category = new Category();
        category.setName("c1");
        session.save(category);

        Product product = (Product) session.get(Product.class,8);
        product.setCategory(category);
        session.update(product);

        session.getTransaction().commit();
        session.close();
        sf.close();
    }
}
