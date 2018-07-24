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

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.entity.Category;
import lightner.sadeqzadeh.lightner.entity.Flashcard;
import lightner.sadeqzadeh.lightner.entity.FlashcardDao;
import lightner.sadeqzadeh.lightner.fragment.CategoryHomeFragment;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class CategoryCardViewAdapter extends RecyclerView.Adapter<CategoryCardViewAdapter.ViewHolder> {
    MainActivity mainActivity;
    Context context;
    List<Category> categoryList;
    FlashcardDao flashcardDao;

    public CategoryCardViewAdapter(Context context, MainActivity mainActivity, List<Category> categoryList) {
        this.context = context;
        this.mainActivity = mainActivity;
        this.categoryList = categoryList;
        this.flashcardDao = mainActivity.getDaoSession().getFlashcardDao();
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
        if(category.getLastVisit()!= null){
            PersianDate  persianDate  = new PersianDate(category.getLastVisit().getTime());
            PersianDateFormat persianDateFormat  = new PersianDateFormat("j F  y ");
            holder.lastVisit.setText(String.format("%s %s",holder.lastVisit.getText(),persianDateFormat.format(persianDate)));
        }
        //set stats
        try {
            Date currentDate = new Date();
            QueryBuilder<Flashcard> queryBuilder = flashcardDao.queryBuilder();
            long total = queryBuilder.where(
                    FlashcardDao.Properties.CategoryId.eq(category.getId())).buildCount().count();
            long reviewable = queryBuilder.where(
                    FlashcardDao.Properties.NextVisit.le(currentDate),
                    FlashcardDao.Properties.CategoryId.eq(category.getId())
            ).buildCount().count();
            holder.total.setText(String.format("%s %d",holder.total.getText(),total));
            holder.reviewable.setText(String.format("%s %d",holder.reviewable.getText(),reviewable));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        GradientDrawable shapeDrawable;
        CardView cardView;
        TextView total;
        TextView reviewable;
        TextView lastVisit;
        public ViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.card_view);
            shapeDrawable = (GradientDrawable) itemView.findViewById(R.id.category_header).getBackground();
            total = itemView.findViewById(R.id.total);
            reviewable = itemView.findViewById(R.id.reviewable);
            lastVisit  =  itemView.findViewById(R.id.last_visit);

        }

    }
}
