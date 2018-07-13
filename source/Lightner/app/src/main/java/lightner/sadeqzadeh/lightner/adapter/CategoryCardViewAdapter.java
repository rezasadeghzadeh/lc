package lightner.sadeqzadeh.lightner.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.entity.Category;

public class CategoryCardViewAdapter extends RecyclerView.Adapter<CategoryCardViewAdapter.ViewHolder> {
    Context context;
    List<Category> categoryList;

    public CategoryCardViewAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_card, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category  =  categoryList.get(position);
        holder.name.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            name =  (TextView) itemView.findViewById(R.id.title);
        }

    }
}
