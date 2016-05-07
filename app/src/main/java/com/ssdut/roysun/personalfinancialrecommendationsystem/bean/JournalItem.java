package com.ssdut.roysun.personalfinancialrecommendationsystem.bean;

/**
 * Created by roysun on 16/3/12.
 * 流水条目
 */
public class JournalItem {

    //支出项目
    public static final String MEAL = "餐饮";
    public static final String MEAL_BREAKFAST = "早餐";
    public static final String MEAL_LUNCH = "午餐";
    public static final String MEAL_SUPPER = "晚餐";
    public static final String MEAL_MIDNIGHT_SNACK = "夜宵";
    public static final String MEAL_SNACK = "零食";
    public static final String MEAL_INGREDIENT = "原料";
    public static final String MEAL_OTHERS = "其他";
    public static String meal[] = new String[]{MEAL_BREAKFAST, MEAL_LUNCH, MEAL_SUPPER, MEAL_MIDNIGHT_SNACK, MEAL_SNACK, MEAL_INGREDIENT, MEAL_OTHERS};

    public static final String TRAFFIC = "交通";
    public static final String TRAFFIC_TEXI = "打的";
    public static final String TRAFFIC_BUS = "公交";
    public static final String TRAFFIC_SUBWAY = "地铁";
    public static final String TRAFFIC_TRAIN = "火车";
    public static final String TRAFFIC_SHIP = "船舶";
    public static final String TRAFFIC_PLANE = "飞机";
    public static final String TRAFFIC_PETROL = "加油";
    public static final String TRAFFIC_PARKING = "停车费";
    public static final String TRAFFIC_OTHERS = "其他";
    public static String traffic[] = new String[]{TRAFFIC_TEXI, TRAFFIC_BUS, TRAFFIC_SUBWAY, TRAFFIC_TRAIN, TRAFFIC_SHIP, TRAFFIC_PLANE, TRAFFIC_PETROL, TRAFFIC_PARKING, TRAFFIC_OTHERS};

    public static final String SHOPPING = "购物";
    public static final String SHOPPING_CLOTHES = "服饰";
    public static final String SHOPPING_NECESSICITY = "日用品";
    public static final String SHOPPING_DIGITAL = "数码产品";
    public static final String SHOPPING_COSMETICS = "化妆品";
    public static final String SHOPPING_ELECTRONICS = "电器";
    public static final String SHOPPING_FURNITURE = "家具";
    public static final String SHOPPING_CIGARETTES_WINE = "烟酒";
    public static final String SHOPPING_OTHERS = "其他";
    public static String shopping[] = new String[]{SHOPPING_CLOTHES, SHOPPING_NECESSICITY, SHOPPING_DIGITAL, SHOPPING_COSMETICS, SHOPPING_ELECTRONICS, SHOPPING_FURNITURE, SHOPPING_CIGARETTES_WINE, SHOPPING_OTHERS};

    public static final String ENTERTAINMENT = "娱乐";
    public static final String ENTERTAINMENT_KTV = "唱歌";
    public static final String ENTERTAINMENT_MOVIE = "电影";
    public static final String ENTERTAINMENT_ONLINE_GAME = "网游";
    public static final String ENTERTAINMENT_TV = "电视";
    public static final String ENTERTAINMENT_FITNESS = "健身";
    public static final String ENTERTAINMENT_BATHING = "洗浴";
    public static final String ENTERTAINMENT_TRAVEL = "旅游";
    public static final String ENTERTAINMENT_OTHERS = "其他";
    public static String entertainment[] = new String[]{ENTERTAINMENT_KTV, ENTERTAINMENT_MOVIE, ENTERTAINMENT_ONLINE_GAME, ENTERTAINMENT_TV, ENTERTAINMENT_FITNESS, ENTERTAINMENT_BATHING, ENTERTAINMENT_TRAVEL, ENTERTAINMENT_OTHERS};

    public static final String MEDITATION_EDUCATION = "医教";
    public static final String MEDITATION_EDUCATION_HOSPITAL = "看病买药";
    public static final String MEDITATION_EDUCATION_TRAINNING = "培训考试";
    public static final String MEDITATION_EDUCATION_TEXTBOOK = "学习教材";
    public static final String MEDITATION_EDUCATION_TUTOR = "家教补习";
    public static final String MEDITATION_EDUCATION_OTHERS = "其他";
    public static String meditation_education[] = new String[]{MEDITATION_EDUCATION_HOSPITAL, MEDITATION_EDUCATION_TRAINNING, MEDITATION_EDUCATION_TEXTBOOK, MEDITATION_EDUCATION_TUTOR, MEDITATION_EDUCATION_OTHERS};

    public static final String DAILY_EXPENSE = "居家";
    public static final String DAILY_EXPENSE_WATER_ELECTRICITY_GASOLINE = "水电燃气";
    public static final String DAILY_EXPENSE_CALL = "手机电话";
    public static final String DAILY_EXPENSE_BEAUTY = "美容美发";
    public static final String DAILY_EXPENSE_HOUSE_LOAN = "房款房贷";
    public static final String DAILY_EXPENSE_ACCOMMODATION = "住宿房租";
    public static final String DAILY_EXPENSE_PROPERTY_MANAGEMENT = "物业费用";
    public static final String DAILY_EXPENSE_OTHERS = "其他";
    public static String daily_expense[] = new String[]{DAILY_EXPENSE_WATER_ELECTRICITY_GASOLINE, DAILY_EXPENSE_CALL, DAILY_EXPENSE_BEAUTY, DAILY_EXPENSE_HOUSE_LOAN, DAILY_EXPENSE_ACCOMMODATION, DAILY_EXPENSE_PROPERTY_MANAGEMENT, DAILY_EXPENSE_OTHERS};

    public static final String INVESTMENT = "投资";
    public static final String INVESTMENT_STOCK_FUTURES = "证券期货";
    public static final String INVESTMENT_GOLD = "黄金实物";
    public static final String INVESTMENT_ANTIQUE = "古玩书画";
    public static final String INVESTMENT_LOAN = "投资贷款";
    public static final String INVESTMENT_INSURANCE = "保险";
    public static final String INVESTMENT_OTHERS = "其他";
    public static String investment[] = new String[]{INVESTMENT_STOCK_FUTURES, INVESTMENT_GOLD, INVESTMENT_ANTIQUE, INVESTMENT_LOAN, INVESTMENT_INSURANCE, INVESTMENT_OTHERS};

    public static final String FAVOR_CONTACT = "人情";
    public static final String FAVOR_CONTACT_GIFT = "礼金";
    public static final String FAVOR_CONTACT_ITEM = "物品";
    public static final String FAVOR_CONTACT_PAYMENT_FOR_OTHERS = "代付款";
    public static final String FAVOR_CONTACT_DONATION_FOR_CHARITY = "慈善捐款";
    public static final String FAVOR_CONTACT_OTHERS = "其他";
    public static String favor_contact[] = new String[]{FAVOR_CONTACT_GIFT, FAVOR_CONTACT_ITEM, FAVOR_CONTACT_PAYMENT_FOR_OTHERS, FAVOR_CONTACT_DONATION_FOR_CHARITY, FAVOR_CONTACT_OTHERS};

    public static String expense_category[] = new String[]{MEAL, TRAFFIC, SHOPPING, ENTERTAINMENT, MEDITATION_EDUCATION, DAILY_EXPENSE, INVESTMENT, FAVOR_CONTACT};

    //收入项目
    public static final String SALARY = "工资";
    public static final String STOCK = "股票";
    public static final String BONUS = "奖金";
    public static final String INTERESTS = "利息";
    public static final String DIVIDEND = "分红";
    public static final String SUBSIDY = "补贴";
    public static final String REIMBURSEMENT = "报销";
    public static final String OTHERS = "其他";
    public static String income_category[] = new String[]{SALARY, STOCK, BONUS, INTERESTS, DIVIDEND, SUBSIDY, REIMBURSEMENT, OTHERS};

    //借贷项目
    public static final String BORROW = "借入";
    public static final String LEND = "借出";
    public static final String PAYMENT = "还款";
    public static final String RECEIVEMENT = "收款";
    public static String credit_debit_category[] = new String[]{BORROW, LEND, PAYMENT, RECEIVEMENT};
}
