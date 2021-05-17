//package Tests;
//
//
//import Domain.Result;
//import Persistance.User;
//import Persistance.HibernateUtil;
//import Service.API;
//import Persistance.HibernateUtil;
//import Persistance.ReceiptsEntity;
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.criterion.Restrictions;
//import org.hibernate.query.Query;
//
//import org.hibernate.query.internal.QueryImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//
//public class MyTest {
//
//
//    @BeforeEach
//    public void setUp() {
//        try {
//            API.initTradingSystem();
//        } catch (Exception e) {
//        }
//    }
//
//    //AT-4.1
//    @Test
//    public void loginSuccessTest() {
//
//        //add();
//        //UsersEntity u = (UsersEntity)getOne("user",1121);
//        //System.out.println(u.getUserName());
//        Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
//        sessionTwo.beginTransaction();
//        Criteria crit = sessionTwo.createCriteria(UsersEntity.class);
//        crit.add(Restrictions.eq("userName","Elad"));
//        List<UsersEntity> results = crit.list();
//
//        sessionTwo.getTransaction().commit();
//    }
//
//    public boolean add(){
//        Session sessionOne = HibernateUtil.getSessionFactory().openSession();
//        sessionOne.beginTransaction();
//        // Create new Employee object
//        UsersEntity emp = new UsersEntity("ela",33,1121,1);
//        sessionOne.save(emp);
//        //store the employee id generated for future use
//        Integer empId = emp.getId();
//        sessionOne.getTransaction().commit();
//        return empId>0;
//    }
//
//    public Object getOne(String name,int id) {
//        switch (name) {
//            case "user": {
//                Session sessionTwo = HibernateUtil.getSessionFactory().openSession();
//                sessionTwo.beginTransaction();
//                return (UsersEntity) sessionTwo.get(UsersEntity.class, id);
//            }
//        }
//        return null;
//    }
//}
