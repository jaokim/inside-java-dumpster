/*
 * 
 */
package inside.dumpster.backend.auth;

import com.github.javafaker.Faker;

/**
 *
 * @author Joakim Nordstrom joakim.nordstrom@oracle.com
 */
public class UserRepository {
  public UserInfo getUserInfo(String userId) {
    Faker faker = new Faker();
    UserInfo userInfo = new UserInfo();
    userInfo.setName(faker.name().fullName());
    userInfo.setSubscriberNumber(faker.phoneNumber().subscriberNumber());
    for(int i = 0; i < 1000 ; i++) {
      userInfo.getUserInfo().put("contact_name_"+i, faker.name().fullName());
      userInfo.getUserInfo().put("contact_number_"+i, faker.phoneNumber().phoneNumber());
      userInfo.getUserInfo().put("contact_city_"+i, faker.address().cityName());
      userInfo.getUserInfo().put("contact_bnr"+i, faker.address().buildingNumber());
      
    }
    return userInfo;
  }
}
