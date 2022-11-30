package it.saimao.hminepos;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.awt.*;

public abstract class Perc {

	static {
		gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		rectangle = Screen.getPrimary().getVisualBounds();
	}

	static GraphicsDevice gd;
	static Rectangle2D rectangle;

	public static double p5w() {

		return (gd.getDisplayMode().getWidth() * 5.0) / 100.0;

	}

	public static double getFullWidth() {
		return rectangle.getWidth();
	}

	public static double getFullHeight() {
		return rectangle.getHeight();
	}

	public static double p10w() {

		return (gd.getDisplayMode().getWidth() * 10.0) / 100.0;

	}

	public static double p15w() {

		return (gd.getDisplayMode().getWidth() * 15.0) / 100.0;

	}

	public static double p20w() {

		return (gd.getDisplayMode().getWidth() * 20.0) / 100.0;

	}

	public static double p25w() {

		return (gd.getDisplayMode().getWidth() * 25.0) / 100.0;

	}

	public static double p30w() {

		return (gd.getDisplayMode().getWidth() * 30.0) / 100.0;

	}

	public static double p35w() {

		return (gd.getDisplayMode().getWidth() * 35.0) / 100.0;

	}

	public static double p40w() {

		return (gd.getDisplayMode().getWidth() * 40.0) / 100.0;

	}

	public static double p45w() {

		return (gd.getDisplayMode().getWidth() * 45.0) / 100.0;

	}

	public static double p50w() {

		return (gd.getDisplayMode().getWidth() * 50.0) / 100.0;

	}
	public static double p55w() {

		return (gd.getDisplayMode().getWidth() * 55.0) / 100.0;

	}
	public static double p60w() {

		return (gd.getDisplayMode().getWidth() * 60.0) / 100.0;

	}
	public static double p65w() {

		return (gd.getDisplayMode().getWidth() * 65.0) / 100.0;

	}
	public static double p70w() {

		return (gd.getDisplayMode().getWidth() * 70.0) / 100.0;

	}
	public static double p75w() {

		return (gd.getDisplayMode().getWidth() * 75.0) / 100.0;

	}
	public static double p80w() {

		return (gd.getDisplayMode().getWidth() * 80.0) / 100.0;

	}
	public static double p85w() {

		return (gd.getDisplayMode().getWidth() * 85.0) / 100.0;

	}
	public static double p90w() {

		return (gd.getDisplayMode().getWidth() * 90.0) / 100.0;

	}
	public static double p95w() {

		return (gd.getDisplayMode().getWidth() * 95.0) / 100.0;

	}
	public static double p100w() {

		return (gd.getDisplayMode().getWidth() * 100.0) / 100.0;

	}
	

	// Height 5%
	public static double p5h() {

		return (gd.getDisplayMode().getHeight() * 5.0) / 100.0;

	}
	public static double p10h() {

		return (gd.getDisplayMode().getHeight() * 10.0) / 100.0;

	}
	public static double p15h() {

		return (gd.getDisplayMode().getHeight() * 15.0) / 100.0;

	}
	public static double p20h() {

		return (gd.getDisplayMode().getHeight() * 20.0) / 100.0;

	}
	public static double p25h() {

		return (gd.getDisplayMode().getHeight() * 25.0) / 100.0;

	}
	public static double p30h() {

		return (gd.getDisplayMode().getHeight() * 30.0) / 100.0;

	}
	public static double p35h() {

		return (gd.getDisplayMode().getHeight() * 35.0) / 100.0;

	}
	public static double p40h() {

		return (gd.getDisplayMode().getHeight() * 40.0) / 100.0;

	}
	public static double p45h() {

		return (gd.getDisplayMode().getHeight() * 45.0) / 100.0;

	}
	public static double p50h() {

		return (gd.getDisplayMode().getHeight() * 50.0) / 100.0;

	}
	public static double p55h() {

		return (gd.getDisplayMode().getHeight() * 55.0) / 100.0;

	}
	public static double p60h() {

		return (gd.getDisplayMode().getHeight() * 60.0) / 100.0;

	}
	public static double p65h() {

		return (gd.getDisplayMode().getHeight() * 65.0) / 100.0;

	}
	public static double p70h() {

		return (gd.getDisplayMode().getHeight() * 70.0) / 100.0;

	}
	public static double p75h() {

		return (gd.getDisplayMode().getHeight() * 75.0) / 100.0;

	}
	public static double p80h() {

		return (gd.getDisplayMode().getHeight() * 80.0) / 100.0;

	}
	public static double p85h() {

		return (gd.getDisplayMode().getHeight() * 85.0) / 100.0;

	}
	public static double p90h() {

		return (gd.getDisplayMode().getHeight() * 90.0) / 100.0;

	}

	public static double p95h() {

		return (gd.getDisplayMode().getHeight() * 95.0) / 100.0;

	}
	public static double p100h() {

		return (gd.getDisplayMode().getHeight() * 100.0) / 100.0;

	}

}
