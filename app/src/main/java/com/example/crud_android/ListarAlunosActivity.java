package com.example.crud_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ListarAlunosActivity extends AppCompatActivity {

    private ListView listaView;
    private AlunoDAO dao;
    private List<Aluno> alunos;
    private List<Aluno> alunosFiltrados = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_alunos);

        listaView = findViewById(R.id.lista_alunos);
        dao = new AlunoDAO(this);
        alunos = dao.ObterTodos();
        alunosFiltrados.addAll(alunos);
        //ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunosFiltrados);
        AlunoAdapter adapter = new AlunoAdapter(this, alunosFiltrados);
        listaView.setAdapter(adapter);

//        metodo que cria um menu de contexto

        registerForContextMenu(listaView);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal, menu);
//        verificação pela pesquisa

        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                procuraAlunos(s);
                return false;
            }
        });

        return true;
    }

    //      um menu de contexto

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto, menu);
    }
//    funçao que pesquisa nas listas

    public  void procuraAlunos(String nome){
        alunosFiltrados.clear();
        for(Aluno a:alunos){
            if (a.getNome().toLowerCase().contains(nome.toLowerCase())){
                alunosFiltrados.add(a);
            }
        }
        listaView.invalidateViews();
    }

    public  void excluir(MenuItem item){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
           final Aluno alunoExcluir = alunosFiltrados.get(menuInfo.position);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Atenção")
                    .setMessage("Realmente deseja excluir?")
                    .setNegativeButton("Não", null)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alunosFiltrados.remove(alunoExcluir);
                            alunos.remove(alunoExcluir);
                            dao.excluir(alunoExcluir);
                            listaView.invalidateViews();
                        }
                    }).create();
            dialog.show();
    }
    public  void atualizar(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Aluno alunoAtualizado = alunosFiltrados.get(menuInfo.position);
        Intent it = new Intent(this, MainActivity.class);

        it.putExtra("aluno", alunoAtualizado);
        startActivity(it);
    }

    public void add(MenuItem item) {
        Intent intent = new Intent(ListarAlunosActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        alunos = dao.ObterTodos();
        alunosFiltrados.clear();
        alunosFiltrados.addAll(alunos);
        listaView.invalidateViews();
    }
}
