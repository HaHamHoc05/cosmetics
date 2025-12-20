package adapters;

import cosmetic.entities.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordEncoder implements PasswordEncoder {
    
	 @Override
	    public String encode(String rawPassword) {
	        try {
	            // Sử dụng BCrypt để hash password
	            return org.mindrot.jbcrypt.BCrypt.hashpw(rawPassword, 
	                   org.mindrot.jbcrypt.BCrypt.gensalt(12));
	        } catch (Exception e) {
	            throw new RuntimeException("Lỗi mã hóa mật khẩu: " + e.getMessage());
	        }
	    }

	    @Override
	    public boolean matches(String rawPassword, String encodedPassword) {
	        try {
	            // So sánh mật khẩu thô với mật khẩu đã hash
	            return org.mindrot.jbcrypt.BCrypt.checkpw(rawPassword, encodedPassword);
	        } catch (Exception e) {
	            return false;
	        }
	    }
	}