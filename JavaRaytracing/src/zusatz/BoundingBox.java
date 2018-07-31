package zusatz;

import java.util.ArrayList;
import java.util.List;

import transformation.Transform;
import aufgabe01.AxisAlignedBox;
import aufgabe01.Color;
import aufgabe01.Geometry;
import aufgabe01.Hit;
import aufgabe01.Node;
import aufgabe01.Ray;
import aufgabe01.Triangle;
import b_vorbereitung.Point3;
import b_vorbereitung.Vector3;
import beleuchtung_I.SingleColorMaterial;

/**
 * Das Rendern von Modellen die aus Dreiecks-Meshes bestehen ist sehr
 * aufwaendig. Fuer jeden Strahl m�ssen alle Dreiecke des Modells gepr�ft
 * werden. Je nach Aufbau der Szene gehen die meisten Strahlen jedoch am Modell
 * vorbei. Eine erste Optimierung ist es um das Modell eine Box zu legen. F�r
 * jeden Strahl wird zunaechst gepr�ft ob die Bounding-Box geschnitten wird.
 * Geht der Strahl vorbei, kann das Objekt nicht getroffen werden und kein
 * Dreieck des Modells muss geprueft werden. Wird die Bounding-Box getroffen
 * dann wurde auch potenziell das Modell getroffen, womit die Dreiecke geprueft
 * werden m�ssen. Nimmt das Modell nicht das vollstaendige Bild ein, sollte
 * durch die Bounding-Box das Rendern wesentlich beschleunigt werden.
 * 
 * @author Kosmonaut
 *
 */
public class BoundingBox extends Geometry {
	/**
	 * die Node, die die Box selbst enth�lt selbst
	 */
	public final Node box;
	/**
	 * die ShapeFromFile, die sich in der Box befindet und deren hit()
	 * beschleunigt werden soll
	 */
	public final ShapeFromFile model;

	/**
	 * Eine Box, die um eine ShapeFromFile gelegt wird um sie performanter zu
	 * machen
	 * 
	 * @param model
	 *            die ShapeFromFile, die sich in der Box befindet und deren
	 *            hit() beschleunigt werden soll
	 */
	public BoundingBox(final ShapeFromFile model) {
		super(null);
		this.model = model;

		// lbf und run der BBox berechnen
		Point3 lbf = model.triangles.get(0).a; // dirty aber performanter als in
												// der Schleife lokales
												// KoordSystem erahnen
		Point3 run = model.triangles.get(0).a;
		final Mixer lbfMixer = (new Mixer() {
			@Override
			public double operation(double x, double y) {
				return Math.min(x, y);
			}
		});
		final Mixer runMixer = (new Mixer() {
			@Override
			public double operation(double x, double y) {
				return Math.max(x, y);
			}
		});
		for (Triangle tri : model.triangles) {
			lbf = lbfMixer.mix(lbf, lbfMixer.result(tri));
			run = runMixer.mix(run, runMixer.result(tri));
		}

		// Skalierung und Translation der Node der AABox, welche die BBox
		// darstellt
		Transform transform = new Transform();
		transform = transform.translation(lbf.add(run.sub(lbf).mul(.5)).sub(
				new Point3(0, 0, 0)));
		transform = transform.scale(run.sub(lbf));

		final List<Geometry> geometries = new ArrayList<Geometry>();
		geometries.add(new AxisAlignedBox(model.material));

		this.box = new Node(transform, geometries);

	}

	@Override
	public Hit hit(Ray r) {
//		return box.hit(r);// Bbox sehen
		 if (box.hit(r) != null){
			 return model.hit(r);
		 }
		 return null;
	}

	/**
	 * Klasse nach Template-Method-Pattern zum redundanzlosen Mixen von Punkten/
	 * erkennen von lbf oder run bei einer mini BBox um ein Triangle
	 * 
	 * @author Kosmonaut
	 *
	 */
	public abstract class Mixer {// Template-Method-Pattern
		public Point3 result(final Triangle tri) {
			return mix(tri.a, mix(tri.b, tri.c));
		}

		public Point3 mix(final Point3 p1, final Point3 p2) {
			return new Point3(operation(p1.x, p2.x), operation(p1.y, p2.y),
					operation(p1.z, p2.z));
		}

		public abstract double operation(final double x, final double y);
	}
}
