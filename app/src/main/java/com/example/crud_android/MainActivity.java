package com.example.crud_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText nome;
    private EditText cpf;
    private EditText telefone;
    private AlunoDAO dao;
    private Aluno aluno = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editCpf);
        telefone = findViewById(R.id.editTelefone);
        dao = new AlunoDAO(this);

// inteção de atualizar

        Intent it = getIntent();
        if (it.hasExtra("aluno")){
            aluno = (Aluno) it.getSerializableExtra("aluno");
            nome.setText(aluno.getNome());
            cpf.setText(aluno.getCpf());
            telefone.setText(aluno.getTelefone());
        }
    }
    public void salvar(View view){
        if (aluno == null){
            Aluno aluno = new Aluno();

            aluno.setNome(nome.getText().toString());
            aluno.setCpf(cpf.getText().toString());
            aluno.setTelefone(telefone.getText().toString());
            long nome = dao.inserir(aluno);

            Toast.makeText(this, "Aluno "+nome+" inserido", Toast.LENGTH_LONG);

        }else{

            aluno.setNome(nome.getText().toString());
            aluno.setCpf(cpf.getText().toString());
            aluno.setTelefone(telefone.getText().toString());
                dao.atualizar(aluno);
            Toast.makeText(this, nome+" Atualizado", Toast.LENGTH_LONG);

        }


        Intent intent = new Intent(this, ListarAlunosActivity.class);
        startActivity(intent);

    }
}
