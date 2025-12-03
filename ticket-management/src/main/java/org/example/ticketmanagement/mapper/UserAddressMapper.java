package org.example.ticketmanagement.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.ticketmanagement.pojo.UserAddress;

@Mapper
public interface UserAddressMapper {
    /**
     * 根据用户id查询收货地址
     */
    @Select("select * from user_address where user_id= #{userId}")
    UserAddress getUserAddressByUserId(Long userId);
    /**
     * 根据id查询收货地址
     */
    @Select("select * from user_address where id= #{id}")
    UserAddress getUserAddressById(Long id);
    /**
     * 根据用户ID和地址信息查询是否存在相同地址
     */
    @Select("SELECT COUNT(*) FROM user_address WHERE user_id = #{userId} AND receiver_name = #{receiverName} AND receiver_phone = #{receiverPhone} AND province = #{province} AND city = #{city} AND district = #{district} AND detail_address = #{detailAddress}")
    int countDuplicateAddress(UserAddress userAddress);


    /**
     * 新增收货地址
     */
    @Insert("insert into user_address(user_id,receiver_name,receiver_phone,province,city,district,detail_address,postal_code,create_time,update_time) " +
            "values(#{userId},#{receiverName},#{receiverPhone},#{province},#{city},#{district},#{detailAddress},#{postalCode},#{createTime},#{updateTime})")
    void addUserAddress(UserAddress userAddress);

    /**
     * 根据id修改收货地址
     */
    @Update("update user_address set receiver_name=#{receiverName},receiver_phone=#{receiverPhone},province=#{province},city=#{city},district=#{district},detail_address=#{detailAddress},postal_code=#{postalCode},update_time=#{updateTime} where id=#{id}")
    void updateUserAddressById(UserAddress userAddress);

    /**
     * 根据id删除收货地址
     */
    @Delete("delete from user_address where id= #{id}")
    void deleteUserAddressById(Long id);
}
