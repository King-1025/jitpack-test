package king.max.net;

import com.google.gson.JsonParseException;

import king.max.R;
import king.max.net.entity.Result;
import king.max.utils.ToastUtils;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Description: 观察着对象封装
 */
public abstract class Subs<T> implements Observer<Result<T>> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Result<T> data) {
        onCompleted();
        if (data.getStatus() == Result.SUCCESS) {
            onSuccess(data.getContent());
        } else {
            ToastUtils.show(data.getContent()+"");
            onFail();
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        onCompleted();
        if (e instanceof HttpException) {
            ToastUtils.show(R.string.toast_error_http);
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            ToastUtils.show(R.string.toast_error_connect);
        } else if (e instanceof InterruptedIOException) {
            ToastUtils.show(R.string.toast_error_time_out);
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            ToastUtils.show(R.string.toast_error_data);
        } else {
            //ToastUtils.show(R.string.toast_error_unknown);
            ToastUtils.show(e.toString());
        }
        onFail();
    }

    @Override
    public void onComplete() {

    }

    /**
     * 无论成功或失败都会调用此方法,用于处理加载动画和加载框等,需要时重写
     */
    protected void onCompleted() {

    }

    /**
     * 请求成功
     *
     * @param data 返回数据
     */
    protected abstract void onSuccess(T data);

    /**
     * 请求失败,需要时重写
     */
    protected void onFail() {

    }
}
