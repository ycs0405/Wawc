package com.wise.pubclas;

import android.graphics.Bitmap;
import android.os.Environment;
/**
 * 常量
 * @author honesty
 */
public class Constant {
    /**
     * 服务器地址："http://wiwc.api.wisegps.cn/"
     */
    public static String BaseUrl = "http://wiwc.api.wisegps.cn/";
    /**
     * 图片地址"http://img.wisegps.cn/logo/"
     */
    public static String ImageUrl = "http://img.wisegps.cn/logo/";
    /**
     * 图片路径存储地址
     */
    public static String BasePath = Environment.getExternalStorageDirectory().getPath() + "/wiwc/image/";
    /**
     * 个人头像和其他图片
     */
    public static String picPath = BasePath + "pic/";
    /**
     * 车友圈
     */
    public static String VehiclePath = BasePath + "vehicle/";
    /**
     * 车品牌logo
     */
    public static String VehicleLogoPath = BasePath + "vehicleLogo/";
    /**
     * 车品牌背景
     */
    public static String VehicleSpellPath = BasePath + "vehicleSpell/";
    /**
     * 个人头像
     */
    public static final String UserImage = "UserIcon.jpg";
    /**
     * 救援照片存储
     */
    public static final String ShareImage = "Share.jpg";
    /**
     * 首页背景图片
     */
    public static final String BgImage = "HomeBg.jpg";
	/**
	 * 获取版本信息用到
	 */
	public static String PackageName = "com.wise.wawc";
	
	/**
	 * 违章推送
	 */
	public static String againstPush_key ="againstPush";
	/**
	 * 故障推送
	 */
	public static String faultPush_key ="faultPush";
	/**
	 * 车务提醒
	 */
	public static String remaindPush_key ="remaindPush";
	
	/**
	 * 默认定位中心
	 */
	public static String defaultCenter_key ="defaultCenter";
	/**
	 * SharedPreferences数据共享名称
	 */
	public static final String sharedPreferencesName = "userData";
	
	public static final String DefaultCity = "DefaultCity";
	/**
	 * 定位城市，获取天气油价
	 */
	public static final String LocationCity = "LocationCity";
	/**
	 * 城市编码
	 */
	public static final String LocationCityCode = "LocationCityCode";
	/**
	 * 省份
	 */
	public static final String LocationProvince = "LocationProvince";
	/**
	 * 油价
	 */
	public static final String LocationCityFuel = "LocationCityFuel";
	/**
	 * 获取指定城市4s店所需参数
	 */
	public static final String FourShopParmeter = "FourShopParmeter";
	/**
	 * 用户id
	 */
	public static final String sp_cust_id = "sp_cust_id";
	/**
	 * 用户auth_code
	 */
	public static final String sp_auth_code = "sp_auth_code";
	/**
	 * sp默认车辆
	 */
	public static final String DefaultVehicleID = "DefaultVehicleID";
	/**
	 * QQ登录返回的数据
	 */
	public static String qqUserName = "";
	/**
	 * QQ用户登录之后的头像
	 */
	public static Bitmap UserIcon = null;
	/**
	 * 基础表
	 */
	public static String TB_Base = "TB_Base";
	/**
	 * 车友圈
	 */
	public static String TB_VehicleFriend = "TB_VehicleFriend";
	/**
	 * 爱车故障
	 */
	public static String TB_Faults = "TB_Faults";
	/**
	 * 行程记录
	 */
	public static String TB_TripList = "TB_TripList";
	/**
	 * 单次行程记录详细
	 */
	public static String TB_Trip = "TB_Trip";
	/**
	 * 我的爱车
	 */
	public static String TB_Vehicle = "TB_Vehicle";
	/**
	 * 我的终端
	 */
	public static String TB_Devices = "TB_Devices";
	/**
	 * 我的收藏
	 */
	public static String TB_Collection = "TB_Collection";
	/**
	 * 定位成功发送广播，选择城市用到
	 */
	public static String A_City = "com.wise.wawc.city";
	/**
	 * 登录广播，首页获取车辆用到
	 */
	public static String A_Login = "com.wise.wawc.login";
	
	public static boolean isHideFooter = false;
	/**
	 * 收货人
	 */
	public static String Consignee = "Consignee";
	/**
	 * 收获地址
	 */
	public static String Adress = "Adress";
	/**
	 * 收货人手机
	 */
	public static String Phone = "Phone";
}
