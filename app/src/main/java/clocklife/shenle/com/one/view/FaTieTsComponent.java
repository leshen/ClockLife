package clocklife.shenle.com.one.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import clocklife.shenle.com.R;
import slmodule.guide.view.Component;

/**
 * Created by binIoter on 16/6/17.
 */
public class FaTieTsComponent implements Component {

  @Override public View getView(LayoutInflater inflater) {

    LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.layout_fatie_ts, null);
    ll.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Toast.makeText(view.getContext(), "引导层被点击了", Toast.LENGTH_SHORT).show();
      }
    });
    return ll;
  }

  @Override public int getAnchor() {
    return Component.ANCHOR_BOTTOM;
  }

  @Override public int getFitPosition() {
    return Component.FIT_END;
  }

  @Override public int getXOffset() {
    return 0;
  }

  @Override public int getYOffset() {
    return 10;
  }
}
