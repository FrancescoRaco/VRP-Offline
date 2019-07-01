/**
 * VRP Offline Module
 */
module vrpOffline
{
	//Make the following packages visible outside
	exports core;
	exports test;
	exports test.busExamples;
	exports gui;
	exports utils;

	//Declare the needed libraries
	requires graphhopper.web;
	requires java.json;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires json.simple;
	requires jsprit.core;
}