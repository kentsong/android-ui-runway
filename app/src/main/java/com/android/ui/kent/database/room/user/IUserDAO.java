package com.android.ui.kent.database.room.user;

import com.android.ui.kent.model.database.vo.UserVO;
import java.util.List;

/**
 * Created by Kent on 2017/10/11.
 */

public interface IUserDAO {



    /** 查詢會員數量 **/
    long getCount();
    long getCountByQuery(UserQuery query);

    /** 查詢會員 **/
    List<UserVO> getUserByQuery(UserQuery query);

    /** 清空db **/
    void clearAll();

    /** 新增用戶**/
    void addRandomUser(int amount);



    interface UserQuery{

        UserQuery setGender(int gender);
        UserQuery setAge(int age);
        UserQuery setLimit(int begin, int end);

        String getWhere();
        String[] getArguments();
        String getLimit();
    }

}
