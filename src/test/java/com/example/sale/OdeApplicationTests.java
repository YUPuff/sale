package com.example.sale;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;


@SpringBootTest
class OdeApplicationTests {


//    @Test
//    void recommend(){
//        List<DetailEntity> detailEntities = detailDao.selectList(new QueryWrapper<DetailEntity>());
//        for (DetailEntity detailEntity : detailEntities) {
//            Long userId = detailEntity.getUserId();
//            Long dishId = detailEntity.getDishId();
//            Integer count = detailEntity.getCount();
//            RecommendEntity recommendEntity = recommendDao.selectOne(new LambdaQueryWrapper<RecommendEntity>()
//                    .eq(RecommendEntity::getUserId,userId).eq(RecommendEntity::getDishId,dishId));
//            if (recommendEntity == null){
//                recommendEntity = new RecommendEntity();
//                recommendEntity.setDishId(dishId);
//                recommendEntity.setUserId(userId);
//                recommendEntity.setCount(count);
//                recommendDao.insert(recommendEntity);
//            }else{
//                recommendEntity.setCount(recommendEntity.getCount()+count);
//                recommendDao.update(recommendEntity,new LambdaQueryWrapper<RecommendEntity>()
//                        .eq(RecommendEntity::getUserId,userId).eq(RecommendEntity::getDishId,dishId));
//            }
//        }
//    }

//    @Test
//    public void recommend() throws Exception {
//        Long userId = 982L;
//        MyRecommender myRecommender = MyRecommender.build();
//        // 2. 根据dataModel和指定的相似度量方法生成用户相似度，并创建基于用户的推荐生成器(不完整)
//        MyRecommender.UserBaseRecommender userBaseRecommender = myRecommender.getUserBaseRecommender(RecommenderConstants.SIMILARITY_TANIMOTO);
//        // 3. 根据dataModel和similarity生成用户邻居，完善推荐生成器
//        userBaseRecommender.getNearestUserNeighborhood(100);
//        // 4. 根据生成器创建通用推荐引擎，参数为false表示无偏好值
//        MyRecommender.CommonRecommender commonRecommender = userBaseRecommender.getCommonRecommender(false);
//        // 5. 生成推荐
//        List<RecommendedItem> recommend = commonRecommender.recommend(userId, 10);
//        for (RecommendedItem recommendedItem : recommend) {
//            System.out.println(recommendedItem);
//        }
//
//        // 评估结果
//        MyRecommender.Evaluator evaluator = commonRecommender.rmsEvaluator();
//        double evaluate = evaluator.evaluate(0.7);
//        System.out.println("差值是："+evaluate);
//
//
//    }

//    @Test
//    public void rr() throws Exception {
//        MyRecommender myRecommender = MyRecommender.build();
//        // 2. 根据dataModel和指定的相似度量方法(谷本系数)生成用户相似度，并创建基于用户的推荐生成器(不完整)
//        MyRecommender.UserBaseRecommender userBaseRecommender = myRecommender.getUserBaseRecommender(RecommenderConstants.SIMILARITY_CITY_BLOCK);
//        // 3. 根据dataModel和similarity生成用户邻居，完善推荐生成器
//        userBaseRecommender.getNearestUserNeighborhood(305);
//        // 4. 根据生成器创建通用推荐引擎，参数为false表示无偏好值
//        MyRecommender.CommonRecommender commonRecommender = userBaseRecommender.getCommonRecommender(false);
//        // 5. 生成推荐
//        List<RecommendedItem> recommend = commonRecommender.recommend(2L, 10);
//        for (RecommendedItem recommendedItem : recommend) {
//            System.out.println(recommendedItem);
//        }
//    }

//    @Test
//    public void test(){
//        Calendar calendar = Calendar.getInstance();
//        int today = calendar.get(Calendar.DAY_OF_MONTH);
//        System.out.println(today);
//    }

}
