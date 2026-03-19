package com.erikarakelyan.travelbro;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * ServiceCardAdapter — RecyclerView adapter that binds ServiceCard data
 * to individual card item views with dynamic theming.
 */
public class ServiceCardAdapter extends RecyclerView.Adapter<ServiceCardAdapter.CardViewHolder> {

    private final Context context;
    private final List<ServiceCard> cards;

    public ServiceCardAdapter(Context context, List<ServiceCard> cards) {
        this.context = context;
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_service_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ServiceCard card = cards.get(position);

        // Set texts
        holder.tvTitle.setText(card.getTitle());
        holder.tvSubtitle.setText(card.getSubtitle());

        // Set icon
        holder.ivIcon.setImageResource(card.getIconResId());

        // Apply dynamic card background with rounded corners
        GradientDrawable cardBg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        adjustBrightness(Color.parseColor(card.getBgColorHex()), 1.3f),
                        Color.parseColor(card.getBgColorHex())
                }
        );
        cardBg.setCornerRadius(dpToPx(16));
        holder.cardContainer.setBackground(cardBg);

        // Accent dot / indicator color
        GradientDrawable dot = new GradientDrawable();
        dot.setShape(GradientDrawable.OVAL);
        dot.setColor(Color.parseColor(card.getAccentColorHex()));
        holder.accentDot.setBackground(dot);

        // Icon tint
        holder.ivIcon.setColorFilter(Color.parseColor(card.getAccentColorHex()));

        // Click feedback
        holder.cardContainer.setOnClickListener(v -> {
            animateClick(v);
            Toast.makeText(context, "Opening: " + card.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    /** Simple scale bounce animation on tap */
    private void animateClick(View v) {
        v.animate()
                .scaleX(0.94f).scaleY(0.94f)
                .setDuration(100)
                .withEndAction(() ->
                        v.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                ).start();
    }

    /** Adjusts color brightness by a factor */
    private int adjustBrightness(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = Math.min(hsv[2] * factor, 1f);
        return Color.HSVToColor(hsv);
    }

    private int dpToPx(int dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }

    // ─── ViewHolder ───────────────────────────────────────────────────────────
    static class CardViewHolder extends RecyclerView.ViewHolder {
        View cardContainer;
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvSubtitle;
        View accentDot;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardContainer = itemView.findViewById(R.id.card_container);
            ivIcon        = itemView.findViewById(R.id.iv_card_icon);
            tvTitle       = itemView.findViewById(R.id.tv_card_title);
            tvSubtitle    = itemView.findViewById(R.id.tv_card_subtitle);
            accentDot     = itemView.findViewById(R.id.accent_dot);
        }
    }
}
