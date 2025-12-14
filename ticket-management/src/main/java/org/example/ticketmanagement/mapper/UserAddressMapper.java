package org.example.ticketmanagement.mapper;

import org.apache.ibatis.annotations.*;
import org.example.ticketmanagement.pojo.UserAddress;

import java.util.List;

@Mapper
public interface UserAddressMapper {
    /**
     * 根据用户id查询收货地址（查询第一个）
     */
    @Select("select * from user_address where user_id= #{userId} LIMIT 1")
    UserAddress getUserAddressByUserId(Long userId);

    /**
     * 根据用户id查询所有收货地址
     */
    @Select("select * from user_address where user_id= #{userId} ORDER BY is_default DESC, update_time DESC")
    List<UserAddress> listUserAddressesByUserId(Long userId);

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
    @Insert("insert into user_address(user_id,receiver_name,receiver_phone,province,city,district,detail_address,postal_code,is_default,create_time,update_time) " +
            "values(#{userId},#{receiverName},#{receiverPhone},#{province},#{city},#{district},#{detailAddress},#{postalCode},#{isDefault},#{createTime},#{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addUserAddress(UserAddress userAddress);

    /**
     * 根据id修改收货地址
     */
    @Update("update user_address set receiver_name=#{receiverName},receiver_phone=#{receiverPhone},province=#{province},city=#{city},district=#{district},detail_address=#{detailAddress},postal_code=#{postalCode},is_default=#{isDefault},update_time=#{updateTime} where id=#{id}")
    void updateUserAddressById(UserAddress userAddress);

    /**
     * 根据id删除收货地址
     */
    @Delete("delete from user_address where id= #{id}")
    void deleteUserAddressById(Long id);

    /**
     * 清除用户的默认地址标记
     */
    @Update("update user_address set is_default = 0 where user_id = #{userId}")
    void clearDefaultAddress(Long userId);

    /**
     * 设置默认地址
     */
    @Update("update user_address set is_default = 1 where id = #{addressId}")
    void setDefaultAddress(Long addressId);
}