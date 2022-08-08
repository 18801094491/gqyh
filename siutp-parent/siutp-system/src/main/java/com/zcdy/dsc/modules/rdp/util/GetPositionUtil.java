package com.zcdy.dsc.modules.rdp.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 *  地图点位工具类
 */
public class GetPositionUtil {

    /**
     * 判断点是否在多边形内
     * @param point 检测点
     * @param pts   多边形的顶点
     * @return      点在多边形内返回true,否则返回false
     */
    public static boolean IsPtInPoly(Point2D.Double point, List<Point2D.Double> pts){

        int N = pts.size();
        boolean boundOrVertex = true;
        //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
        int intersectCount = 0;
        //cross points count of x
        double precision = 2e-10;
        //浮点类型计算时候与0比较时候的容差
        Point2D.Double p1, p2;
        //neighbour bound vertices
        Point2D.Double p = point;
        //当前点

        p1 = pts.get(0);
        //left vertex
        for(int i = 1; i <= N; ++i){
            //check all rays
            if(p.equals(p1)){
                return boundOrVertex;
                //p is an vertex
            }

            p2 = pts.get(i % N);
            //right vertex
            if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){
                //ray is outside of our interests
                p1 = p2;
                continue;
                //next ray left point
            }

            if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){
                //ray is crossing over by the algorithm (common part of)
                if(p.y <= Math.max(p1.y, p2.y)){
                    //x is before of ray
                    if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){
                        //overlies on a horizontal ray
                        return boundOrVertex;
                    }

                    if(p1.y == p2.y){
                        //ray is vertical
                        if(p1.y == p.y){
                            //overlies on a vertical ray
                            return boundOrVertex;
                        }else{
                            //before ray
                            ++intersectCount;
                        }
                    }else{
                        //cross point on the left side
                        //cross point of y
                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;
                        if(Math.abs(p.y - xinters) < precision){
                            //overlies on a ray
                            return boundOrVertex;
                        }

                        if(p.y < xinters){
                            //before ray
                            ++intersectCount;
                        }
                    }
                }
                //special case when ray is crossing through the vertex
            }else{
                //p crossing over p2
                if(p.x == p2.x && p.y <= p2.y){
                    //next vertex
                    Point2D.Double p3 = pts.get((i+1) % N);
                    //p.x lies between p1.x & p3.x
                    if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){
                        ++intersectCount;
                    }else{
                        intersectCount += 2;
                    }
                }
            }
            //next ray left point
            p1 = p2;
        }

        //偶数在多边形外
        if(intersectCount % 2 == 0){
            return false;
        } else {
            //奇数在多边形内
            return true;
        }

    }




}
