package buildLight.views

import java.awt.Point
import javax.swing.JDialog

class JCenteredDialog extends JDialog {

    public JCenteredDialog(owner) {
        super(owner)
    }

    @Override
    public void show() {

        def location = new Point()

        location.x = this.parent.location.x + (this.parent.size.width - this.size.width) / 2
        location.y = this.parent.location.y + (this.parent.size.height - this.size.height) / 2

        location.x = location.x > 0 ? location.x : 0;
        location.y = location.y > 0 ? location.y : 0;

        this.location = location

        super.show()
    }

}