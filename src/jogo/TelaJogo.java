package jogo;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class TelaJogo extends JFrame implements ActionListener {

    private final JPanel painelImagens, painelPontos;
    private final JButton botaoApostar, botaoLimpar, botaoChance, botaoSair;
    private final Icon imagemApostar, imagemLimpar, imagemChances, imagemSair;
    private final Icon[] indigo, azul ,rubro, ouro;
    private final GridLayout grid, gridBotoes;
    private final Container container;
    private final int numerosKino = 80, apostaMinima = 5, apostaMaxima = 15;
    int[] numerosCartela, acertos, sorteados;
    int palpite = 0, bola =0;
    private boolean proxConc = false;
    private final JButton  posicoesFiguras[];
    Random numeroSorteado = new Random();

    //===================================
    //
    public TelaJogo( ){
        super( "Kino 0.1" );
        acertos = new int[apostaMinima];
        sorteados = new int[apostaMinima];
        numerosCartela = new int[numerosKino];
        container = getContentPane();

        imagemApostar = new ImageIcon(getClass().getResource("/img/apostar.gif"));
        botaoApostar = new JButton("Apostar",imagemApostar);
        botaoApostar.addActionListener(this);

        imagemLimpar = new ImageIcon(getClass().getResource("/img/limpar.gif"));
        botaoLimpar = new JButton("Limpar", imagemLimpar  );
        botaoLimpar.addActionListener(this);

        imagemChances = new ImageIcon(getClass().getResource("/img/chances.gif"));
        botaoChance = new JButton("Chances", imagemChances);
        botaoChance.addActionListener(this);

        imagemSair = new ImageIcon(getClass().getResource("/img/sair.gif"));
        botaoSair = new JButton(" Sair", imagemSair);
        botaoSair.addActionListener( this );

        painelImagens = new JPanel();
        painelPontos = new JPanel();

        grid = new GridLayout( 8, 10, 3, 3);
        gridBotoes = new GridLayout( 1, 3, 10, 10 );

        painelPontos.setLayout( gridBotoes );
        painelImagens.setLayout( grid );
        painelImagens.setBackground(Color.darkGray);
        indigo = new Icon[numerosKino];
        azul  = new Icon[numerosKino];
        rubro = new Icon[numerosKino];
        ouro = new Icon[numerosKino];

        posicoesFiguras = new JButton[ numerosKino ];

        for ( int i = 0; i <  numerosKino ; i++ ){

            indigo[ i ] = new ImageIcon(getClass().getResource("/img/indigo/"+ ( i + 1 ) + ".gif"));
            azul [ i ] = new ImageIcon(getClass().getResource("/img/azul/"+ ( i + 1 ) + ".gif"));
            rubro[ i ] = new ImageIcon(getClass().getResource("/img/rubro/"+ ( i + 1 ) + ".gif"));
            ouro[ i ] = new ImageIcon(getClass().getResource("/img/ouro/"+ ( i + 1 ) + ".gif"));
            this.posicoesFiguras[ i ] = new JButton();
            this.posicoesFiguras[ i ].setName(Integer.toString( i ));
            posicoesFiguras[ i ].setIcon(indigo[ i ]);
            posicoesFiguras[ i ].setActionCommand(posicoesFiguras[ i ].getName());
            posicoesFiguras[ i].addActionListener( this );
            painelImagens.add( posicoesFiguras[ i ] );
        }
        container.add( painelPontos, BorderLayout.NORTH);
        painelPontos.add(botaoApostar);
        painelPontos.add(botaoLimpar);
        painelPontos.add(botaoChance);
        painelPontos.add(botaoSair);
        container.add( painelImagens );
        setSize( 750, 650 );
        setVisible( true );
        setResizable( false );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocationRelativeTo( null );
    } //fim do construtor TelaJogo

    public void actionPerformed( ActionEvent evento ){
        if ( evento.getSource() == botaoSair ){
            System.exit( 0 );
        }
        else if(evento.getSource() == botaoChance){
            chances();
        }
        else if(evento.getSource() == botaoLimpar){
            limpar();
        }
        else if(evento.getSource() == botaoApostar){
            int contaAcertos =0;
            if (palpite < apostaMinima)
                JOptionPane.showMessageDialog(painelImagens, "A aposta mínima é de 5 números.");
            else{
                for (int i = 0; i < apostaMinima; i++) {
                    do {
                        bola = numeroSorteado.nextInt(numerosKino);
                    } while (numerosCartela[bola] % 2 == 1);
                    if (numerosCartela[bola] == 2) {
                        numerosCartela[bola] = 3;
                        acertos[contaAcertos] = bola + 1; //adiciona ao array acertos
                        contaAcertos++;
                    } else {
                        numerosCartela[bola] = 1; // marca a posição com 1 (já sorteado)
                    }
                    sorteados[i] = bola + 1; //adiciona ao array sorteados
                }
                pintar();
            }
        }

        //-----------------------------------------------
        // Caso contrário é evento de botões de imagem
        // BOTOES 1 a numerosKino
        else {
            int i=Integer.parseInt(evento.getActionCommand());
            marcar(i);
        }
    } //fim do método actionPerformed

    //===================================
    //
    void marcar(int i){
        if(!proxConc){
    //      desmarca
            if (numerosCartela[i] == 2) {
                posicoesFiguras[i].setIcon(indigo[i]);
                numerosCartela[i] = 0;
                palpite--;
            }
            else if (palpite >= apostaMaxima)
                JOptionPane.showMessageDialog(painelImagens, "Atingiu a aposta máxima.");
    //      marca
            else {
                posicoesFiguras[i].setIcon(azul[i]);
                numerosCartela[i] = 2;
                palpite++;
            }
        }
        else
        {
            limpar();
        }
    } //fim do método marcar

    //===================================
    //
    void pintar(){
        if(!proxConc){
            painelImagens.setBackground(Color.lightGray);
            for (int i = 0; i < numerosCartela.length; i++) {
                if (numerosCartela[i] == 2) {//apostou
                    numerosCartela[i] = 0;//desmarca a posicao
                } else if (numerosCartela[i] == 3) {//acertou
                    posicoesFiguras[i].setIcon(ouro[i]);
                    numerosCartela[i] = 0;
                } else if (numerosCartela[i] == 1) {//errou
                    posicoesFiguras[i].setIcon(rubro[i]);
                    numerosCartela[i] = 0;
                } else {//desativa demais icones
                    posicoesFiguras[i].setEnabled(false);
                }
            }
            proxConc = true;
        }
        else
        {
            limpar();
        }
    } //fim do método pintar

//===================================
    //

    void limpar(){
        painelImagens.setBackground(Color.darkGray);
        for (int i = 0; i < numerosCartela.length; i++) {
            numerosCartela[i] = 0;
            posicoesFiguras[i].setIcon(indigo[i]);
            posicoesFiguras[i].setEnabled(true);
        }
        palpite = 0;
        proxConc = false;
    } //fim do método limpar


    void chances(){
        JOptionPane.showMessageDialog(painelImagens, "Conforme a aposta, uma chance em:\n" +
                "\n" +
                "Núm.----Kino------Kuadro-----Trino-----Duki\n" +
                " 5:  24.040.016      64.106        866        36\n" +
                " 6:    4.006.669      21.658        445        25\n" +
                " 7:    1.144.763        9.409        261       18\n" +
                " 8:       429.286        4.770        168        14\n" +
                " 9:       190.794        2.687        115        12\n" +
                "10:        95.396        1.635         82          9\n" +
                "11:        52.035        1.056         62          8\n" +
                "12:        30.354           714         48          7\n" +
                "13:        18.679           502         38          6\n" +
                "14:        12.008           364         31         5,8\n" +
                "15:          8.005           271         25         5,2\n\n"+
                "\tKino 0.1 - 11/2021 \n\treinaldo589@hotmail.com","Probabilidades",1);


    }


    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaJogo().setVisible(true);
            }
        });

    }// fim da classe main

} // fim da classe TelaJogo
