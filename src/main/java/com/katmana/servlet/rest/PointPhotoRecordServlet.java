package com.katmana.servlet.rest;

import javax.servlet.annotation.WebServlet;

import com.katmana.model.PointPhoto;
import com.katmana.model.rest.PointPhotoRestConfiguration;

@WebServlet("point_photo/*")
public class PointPhotoRecordServlet extends BaseRecordServlet<PointPhoto, PointPhotoRestConfiguration>{

}
