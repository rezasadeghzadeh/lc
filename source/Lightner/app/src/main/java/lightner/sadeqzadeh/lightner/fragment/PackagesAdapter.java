package lightner.sadeqzadeh.lightner.fragment;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.rest.LightnerPackage;

public class PackagesAdapter  extends RecyclerView.Adapter<PackagesAdapter.ViewHolder> {
    MainActivity mainActivity;
    Context context;
    List<LightnerPackage> lightnerPackages;

    public PackagesAdapter(Context context, MainActivity mainActivity, List<LightnerPackage> lightnerPackages) {
        this.context = context;
        this.mainActivity = mainActivity;
        this.lightnerPackages = lightnerPackages;
    }

    @Override
    public PackagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_card, parent, false);
        return new PackagesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PackagesAdapter.ViewHolder holder, int position) {
        LightnerPackage lightnerPackage = lightnerPackages.get(position);
        holder.name.setText(lightnerPackage.getName());
        holder.price.setText(String.format("%s %s %s",holder.price.getText(),lightnerPackage.getPrice(), context.getString(R.string.toman)));
    }

    @Override
    public int getItemCount() {
        return lightnerPackages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        CardView cardView;
        TextView price;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.card_view);
            price = itemView.findViewById(R.id.price);
        }
    }

}
