package com.wmy.main.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wmy.main.R;

import java.util.HashMap;
import java.util.List;


public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    private Builder builder;

    public Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private RadioGroup radioGroup;
        private String positiveButtonText;
        private String negativeButtonText;
        private String radioSelectText;
        private String radioSelectCode;
        private List list;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public String getRadioSelectText() {
            return radioSelectText;
        }

        private void setRadioSelectText(String radioSelectText) {
            this.radioSelectText = radioSelectText;
        }

        public String getRadioSelectCode() {
            return radioSelectCode;
        }

        private void setRadioSelectCode(String radioSelectCode) {
            this.radioSelectCode = radioSelectCode;
        }

        //        public RadioGroup getRadioGroup(){
//            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//
//                }
//            });
//            return radioGroup;
//        }
        public Builder setList(List list) {
            this.list = list;
            return this;
        }

        public Builder setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener onCheckedChangeListener) {
            this.onCheckedChangeListener = onCheckedChangeListener;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.CustomAlertDialogBackground);
            View layout = inflater.inflate(R.layout.common_cancel_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if(title!=null)
                ((TextView) layout.findViewById(R.id.tv_dialog_title)).setText(title);
            else
                layout.findViewById(R.id.ll_title).setVisibility(View.GONE);

            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_ok))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_ok))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_ok).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_cancel))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_cancel))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_cancel).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (message != null) {
                layout.findViewById(R.id.message).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (list != null) {
                layout.findViewById(R.id.message).setVisibility(View.GONE);

            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                layout.findViewById(R.id.message).setVisibility(View.GONE);
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            }
            dialog.setContentView(layout);
            return dialog;
        }

        public CustomDialog create(int layout_id) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.CustomAlertDialogBackground);
            View layout = inflater.inflate(layout_id, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if(title!=null)
                ((TextView) layout.findViewById(R.id.tv_dialog_title)).setText(title);
            else
                layout.findViewById(R.id.ll_title).setVisibility(View.GONE);

            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_ok))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_ok))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_ok).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_cancel))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_cancel))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_cancel).setVisibility(
                        View.GONE);
            }
            if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                layout.findViewById(R.id.message).setVisibility(View.GONE);
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            }
            dialog.setContentView(layout);
            return dialog;
        }
    }

}
