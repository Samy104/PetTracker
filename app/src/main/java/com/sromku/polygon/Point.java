package com.sromku.polygon;

import com.google.android.gms.maps.model.LatLng;

/**
 * Point on 2D landscape
 * 
 * @author Roman Kushnarenko (sromku@gmail.com)</br>
 */
public class Point
{
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Point(LatLng latLng) {
		this.x = latLng.latitude;
		this.y = latLng.longitude;
	}

	public double x;
	public double y;

	@Override
	public String toString()
	{
		return String.format("(%.2f,%.2f)", x, y);
	}
}