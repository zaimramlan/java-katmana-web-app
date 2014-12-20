package com.katmana.servlet.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.katmana.model.PointPhoto;
import com.katmana.model.rest.PointPhotoRestConfiguration;

@WebServlet("point_photos")
@MultipartConfig(maxFileSize = 10*1024*1024,maxRequestSize = 20*1024*1024,fileSizeThreshold = 5*1024*1024)
public class PointPhotoIndexServlet extends BaseIndexServlet<PointPhoto, PointPhotoRestConfiguration>{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendError(404, "Nothing to see here. Move along.");
	}

}
