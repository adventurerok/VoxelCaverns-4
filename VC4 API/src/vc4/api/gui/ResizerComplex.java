package vc4.api.gui;

import java.awt.Rectangle;

public class ResizerComplex implements Resizer {

	public static interface Part {
		public int resize(Component target);
	}

	Part[] parts = new Part[4];

	public ResizerComplex(Part mix, Part miy, Part max, Part may) {
		parts[0] = mix;
		parts[1] = miy;
		parts[2] = max;
		parts[3] = may;
	}

	@Override
	public void resize(Component target) {
		Rectangle n = (Rectangle) target.getBounds().clone();
		if (parts[0] != null) n.x = parts[0].resize(target);
		if (parts[1] != null) n.y = parts[1].resize(target);
		if (parts[2] != null) n.width = parts[2].resize(target) - n.x;
		if (parts[3] != null) n.height = parts[3].resize(target) - n.y;
		target.setBounds(n);
	}

	public static class PartConstant implements Part {

		int var;

		@Override
		public int resize(Component target) {
			return var;
		}

		public PartConstant(int var) {
			super();
			this.var = var;
		}

	}

	public static class PartPercentX implements Part {

		int percent;

		@Override
		public int resize(Component target) {
			return (int) ((target.getParent().getBounds().getMinX() * (1 - percent)) + (target.getParent().getBounds().getMaxX() * percent));
		}

		public PartPercentX(int percent) {
			super();
			this.percent = percent;
		}

	}

	public static class PartPercentY implements Part {

		int percent;

		@Override
		public int resize(Component target) {
			return (int) ((target.getParent().getBounds().getMinY() * (1 - percent)) + (target.getParent().getBounds().getMaxY() * percent));
		}

		public PartPercentY(int percent) {
			super();
			this.percent = percent;
		}

	}

	public static class PartAddX implements Part {
		int add;

		@Override
		public int resize(Component target) {
			return target.getParent().getX() + add;
		}

		public PartAddX(int add) {
			super();
			this.add = add;
		}
	}

	public static class PartAddY implements Part {
		int add;

		@Override
		public int resize(Component target) {
			return target.getParent().getY() + add;
		}

		public PartAddY(int add) {
			super();
			this.add = add;
		}
	}

	public static class PartSubX implements Part {
		int sub;

		@Override
		public int resize(Component target) {
			return (int) (target.getParent().getBounds().getMaxX() - sub);
		}

		public PartSubX(int sub) {
			super();
			this.sub = sub;
		}
	}

	public static class PartSubY implements Part {
		int sub;

		@Override
		public int resize(Component target) {
			return (int) (target.getParent().getBounds().getMaxY() - sub);
		}

		public PartSubY(int sub) {
			super();
			this.sub = sub;
		}
	}

}
