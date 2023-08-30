package hiber.dao;
;
import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public User findUserWithCar(String model, int series) {
       Query query = sessionFactory.getCurrentSession()
               .createQuery("from Car where model = :model and series = :series")
               .setParameter("model", model)
               .setParameter("series", series);
       List<Car> carFindList = query.getResultList();
       if (!carFindList.isEmpty()) {
           Car carFind = carFindList.get(0);
           List<User> userList = listUsers();
           return userList.stream().filter(user -> user.getCar().equals(carFind)).findAny().orElse(null);
       }
       return null;
   }
}
