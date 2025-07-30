package config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {
        private String firstname;
        private String lastname;
        private String email;
        private String password;
        private String phonenumber;
        private String address;

        public UserModel(String firstname,String lastname,String email,String password,String phonenumber,String address){
                this.firstname=firstname;
                this.lastname=lastname;
                this.email=email;
                this.password=password;
                this.phonenumber=phonenumber;
                this.address=address;
        }

        public UserModel() {

        }
}
