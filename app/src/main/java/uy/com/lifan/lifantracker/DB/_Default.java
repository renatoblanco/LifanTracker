package uy.com.lifan.lifantracker.DB;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Renato Blanco on  31/08/2015
 */
public class _Default extends AppCompatActivity {
    protected String _mensagem;
    protected boolean _status;

    public _Default(){
        this._status = true;
        this._mensagem = "";
    }

    public String get_mensagem() {
        return _mensagem;
    }

    public void set_mensagem(String _mensagem) {
        this._mensagem = _mensagem;
    }

    public boolean is_status() {
        return _status;
    }

    public void set_status(boolean _status) {
        this._status = _status;
    }
}
