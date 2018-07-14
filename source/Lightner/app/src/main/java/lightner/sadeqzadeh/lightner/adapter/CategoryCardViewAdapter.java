package lightner.sadeqzadeh.lightner.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.entity.Category;
import lightner.sadeqzadeh.lightner.fragment.CategoryHomeFragment;

public class CategoryCardViewAdapter extends RecyclerView.Adapter<CategoryCardViewAdapter.ViewHolder> {
    MainActivity mainActivity;
    Context context;
    List<Category> categoryList;

    public CategoryCardViewAdapter(Context context, MainActivity mainActivity, List<Category> categoryList) {
        this.context = context;
        this.mainActivity = mainActivity;
        this.categoryList = categoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Category category  =  categoryList.get(position);
        holder.name.setText(category.getName());
        int color=0;
        try{
            color  =  Integer.parseInt(category.getCodeColor());
        }catch (Exception e){

        }
        holder.shapeDrawable.setColor(color);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putLong(Const.CATEGORY_ID,category.getId());
                CategoryHomeFragment  categoryHomeFragment  = new CategoryHomeFragment();
                categoryHomeFragment.setArguments(args);
                mainActivity.replaceFragment(categoryHomeFragment, CategoryHomeFragment.TAG);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        GradientDrawable shapeDrawable;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            name =  (TextView) itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.card_view);
            shapeDrawable  = (GradientDrawable) itemView.findViewById(R.id.category_header).getBackground();
        }

    }
}
