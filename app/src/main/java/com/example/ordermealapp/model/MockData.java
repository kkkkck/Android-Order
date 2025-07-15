package com.example.ordermealapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.example.ordermealapp.R;
public class MockData {

    public static List<Store> getMockStores() {
        List<Store> stores = new ArrayList<>();
        stores.add(new Store(1, "美味中餐厅", R.drawable.a1, 4.5));
        stores.add(new Store(2, "幸福西餐厅", R.drawable.a2, 4.8));
        stores.add(new Store(3, "天天快餐店", R.drawable.a5, 4.2));
        // Add more stores to make it resemble Meituan
        stores.add(new Store(4, "日式居酒屋", R.drawable.a4, 4.7));
        stores.add(new Store(5, "特色烧烤店", R.drawable.a3, 4.6));
        return stores;
    }

    public static List<Dish> getMockDishes() {
        List<Dish> dishes = new ArrayList<>();
        // Dishes for store 1 (美味中餐厅)
        dishes.add(new Dish(101, "宫保鸡丁", "经典川菜，味道鲜美", 38.0, R.drawable.gongbaojiding, 1));
        dishes.add(new Dish(102, "鱼香肉丝", "酸甜可口，非常下饭", 32.0, R.drawable.yuxiang, 1));
        dishes.add(new Dish(103, "麻婆豆腐", "麻辣鲜香，传统名菜", 28.0, R.drawable.mapo, 1));

        // Dishes for store 2 (幸福西餐厅)
        dishes.add(new Dish(201, "黑椒牛排", "七分熟，肉质鲜嫩", 88.0, R.drawable.niupai, 2));
        dishes.add(new Dish(202, "意大利面", "浓郁番茄肉酱", 45.0, R.drawable.yimian, 2));
        dishes.add(new Dish(203, "凯撒沙拉", "健康美味", 35.0, R.drawable.shala, 2));

        // Dishes for store 3 (天天快餐店)
        dishes.add(new Dish(301, "香辣鸡腿饭", "酥脆鸡腿，香辣过瘾", 25.0, R.drawable.jitui, 3));
        dishes.add(new Dish(302, "卤肉饭", "经典台式风味", 22.0, R.drawable.lurou, 3));

        // Dishes for store 4 (日式居酒屋)
        dishes.add(new Dish(401, "三文鱼寿司", "新鲜三文鱼，入口即化", 68.0, R.drawable.shousi, 4));
        dishes.add(new Dish(402, "章鱼小丸子", "外酥里嫩，味道浓郁", 25.0, R.drawable.zhangyu, 4));

        // Dishes for store 5 (特色烧烤店)
        dishes.add(new Dish(501, "羊肉串", "新疆风味，香气扑鼻", 8.0, R.drawable.yangrou, 5));
        dishes.add(new Dish(502, "烤生蚝", "蒜蓉粉丝，鲜美多汁", 12.0, R.drawable.shenghao, 5));

        return dishes;
    }

    public static List<Dish> getDishesByStoreId(int storeId) {
        return getMockDishes().stream()
                .filter(dish -> dish.getStoreId() == storeId)
                .collect(Collectors.toList());
    }
}