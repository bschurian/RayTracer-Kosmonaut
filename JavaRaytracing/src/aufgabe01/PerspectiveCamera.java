package aufgabe01;

import b_vorbereitung.Point3;
import b_vorbereitung.Vector3;

/**
 * Diese Klasse stellt eine perspektivische Kamera dar
 * @author Kosmonaut
 *
 */
public class PerspectiveCamera extends Camera {

	/**
	 * Der Öffnungswinkel der Kamera
	 */
	public final double angle;
	
	/**
	 * Dieser Konstruktor erzeugt ein neues PerspectiveCamera-Objekt
	 * @param e Position der Kamera
	 * @param g Blickrichtung der Kamera
	 * @param t Up-Vektor der Kamera
	 * @param angle Öffnungswinkel der Kamera
	 */
	public PerspectiveCamera(final Point3 e, final Vector3 g, final Vector3 t, final double angle) {
		super(e, g, t);
		this.angle = angle;
	}

	@Override
	public Ray rayFor(final int w, final int h, final int x, final int y) {
		final Vector3 vectorX = this.u.mul(x - (((double)w-1)/2));
		final Vector3 vectorY = this.v.mul(y - (((double)h-1)/2));
		double tanA = Math.tan(angle);
		if(tanA == 0) throw new IllegalArgumentException();
		final Vector3 r = this.w.mul(((double)h/2)/tanA).mul(-1).add(vectorX).add(vectorY);
		
		return new Ray(e, r.normalized());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(angle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PerspectiveCamera other = (PerspectiveCamera) obj;
		if (Double.doubleToLongBits(angle) != Double
				.doubleToLongBits(other.angle))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PerspectiveCamera [angle=" + angle + "]";
	}
	

}
