package cn.dlc.yinrongshouhuoji.util;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 工具类
 * Created by Administrator on 2017/11/17.
 */

public class Utils {

    public static int getSize(StringBuffer stringBuffer) {
        StringBuffer sub = new StringBuffer(stringBuffer);
        int r = 0;
        String s = ",";
        for (int i = 0; i < sub.length(); i++) {
            if (sub.indexOf(s, i) != -1) {
                i = sub.indexOf(s, i);
                r++;
            }
        }
        return r;

    }

    /*
* 将时间戳转换为时间
*/
    public static String stampToDate(String s) {
//        String res;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        long lt = new Long(s);
//        Date date = new Date(lt);
//        res = simpleDateFormat.format(date);
        String formats = "yyyy-MM-dd ";
        Long timestamp = Long.parseLong(s) * 1000;
        String res = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return res;
    }

    public static String NewToDate(String s) {
//        String res;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        long lt = new Long(s);
//        Date date = new Date(lt);
//        res = simpleDateFormat.format(date);
        return StringToDate(s, "yyyy-MM-dd", "yyyy-MM-dd");
    }

    public static String StringToDate(String dateStr, String dateFormatStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(dateFormatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);

        return s.format(date);
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }


    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public static String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    /*
   * 将时间戳转换为时间
   */
    public static String stampToDateDetail(String s) {
        String formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(s) * 1000;
        String res = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return res;
    }


    public static String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }


    public static String data(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("MM/dd",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String getCurrentTime() {
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = tempDate.format(new Date(System.currentTimeMillis()));
        return datetime;
    }


    /**
     * 从时间(毫秒)中提取出时间(时:分:秒)
     *
     * @param millisecond
     * @return
     */
    public static String getTimeFromMillisecond(Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(millisecond);
        String timeStr = simpleDateFormat.format(date);
        return timeStr;
    }

    /**
     * 获取日期
     *
     * @return
     */
    public static String getDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new Date());
        return date;
    }


}
