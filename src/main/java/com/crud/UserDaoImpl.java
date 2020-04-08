package com.crud;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import redis.clients.jedis.Jedis;
import java.util.*;
import java.net.URI;


/**
 * 技术点：
 * 1.采取的方式是反向索引，在redis中存储查询列表，加快查询所有，查询姓名的速度
 * 2.fastJSON 进行对象和JSON格式字符串的转换，通过get set 直接存储JSON格式字符串
 */
public class UserDaoImpl implements UserDao {
    private Jedis jedis;

    /**
     * 打开连接
     * @return
     * @throws Exception
     */
    private Jedis openConnection()throws Exception{
        return new Jedis(new URI("redis://localhost:6379/8"));
    }

    //生成随机ID
    private String generateId(){
        return System.currentTimeMillis()+"";
    }

    public UserDaoImpl(){
        Jedis jedis = new Jedis("127.0.0.1",6379);
        jedis.auth("wsuser");
        this.jedis = jedis;
    }

    /**
     * 查询所有
     * @return
     */
    public List<UserVO> queryAll() {
//        Set<String> set= jedis.keys("*");//不可取
        String globalKey = UserVO.class.getName();
        long length = jedis.llen(globalKey);
        List<String> idList = jedis.lrange(globalKey, 0, length - 1);//
        List<UserVO> userList = new ArrayList<>();
        for(String id : idList){
            UserVO vo = queryById(id);
            userList.add(vo);
        }
        return userList;
    }

    /**
     * 姓名查询
     * @param userName
     * @return
     */
    public List<UserVO> queryByName(String userName) {
        long length = jedis.llen(userName);//获取查询后的列表长度
        List<String> idList = jedis.lrange(userName, 0, length - 1);
        List<UserVO> userList = new ArrayList<>();
        for(String id : idList){
            UserVO vo = queryById(id);
            userList.add(vo);
        }
        return userList;
    }

    /**
     * Id查询
     * @param userId
     * @return
     */
    public UserVO queryById(String userId) {
        return JSON.parseObject(jedis.get(userId), new TypeReference<UserVO>() {});
    }

    /**
     * 插入
     * @param user
     */
    public void insert(UserVO user) {
        user.setUserId("USER:"+generateId());
        jedis.set(user.getUserId(),JSON.toJSONString(user));
        //将每个索引加入redis列表中，方便全部查询
        String globalKey = UserVO.class.getName();
        jedis.lpush(globalKey, user.getUserId());

        //以查询姓名为键，索引为值，加入redis列表中，方便查询姓名
        String nameKey = user.getUserName();
        jedis.lpush(nameKey, user.getUserId());
    }

    /**
     * 更新
     * @param user
     */
    public void update(UserVO user) {
        Long along = jedis.del(user.getUserId());
        if(along<0){
            System.out.println("删除失败");
            return;
        }
        UserVO vo = JSON.parseObject(jedis.get(user.getUserId()), new TypeReference<UserVO>() {});
        String oldName = vo.getUserName();
        String newName = vo.getUserName();
        jedis.set(user.getUserId(),JSON.toJSONString(user));

        //如果名字更新，查询列表也需要更新
        if(!newName.equals(oldName)){
            jedis.lrem(oldName, 1, user.getUserId());
            jedis.lpush(newName, user.getUserId());
        }
    }

    /**
     * 删除
     * @param user
     */
    public void delete(UserVO user) {
        //删除查询列表Id
        UserVO vo = JSON.parseObject(jedis.get(user.getUserId()), new TypeReference<UserVO>() {});
        String userName = vo.getUserName();
        jedis.lrem(userName, 1, user.getUserId());
        //删除姓名查询列表
        String globalKey = UserVO.class.getName();
        jedis.lrem(globalKey,1 , user.getUserId());
        jedis.del(user.getUserId());
    }
}
