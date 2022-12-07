/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package com.aliu.ev3.ev3;




public class EV3Brick {
    BluetoothEV3Service os;
    
    EV3Brick(BluetoothEV3Service mEV3Service) {
        os = mEV3Service;
    }
    
    public void send(String n, String s) {
    	//define cmd byte   n.length()+11 bytes +s.length()
        byte[] cmd = new byte[n.length() + 0xb + s.length()];
      //initiate byte position
        int pos = 0x0;
        //stringsS
        //��¼�ַ���
        cmd[0x0] = (byte)((cmd.length - 0x2) & 0xff);
        //�ַ�������8λ����λ
        cmd[0x1] = (byte)((cmd.length - 0x2) >> 0x8);
        //00
        cmd[0x2] = 0x0;
        //00
        cmd[0x3] = 0x0;
        //81
        cmd[0x4] = (byte) 0x81;
        //9E
        cmd[0x5] = (byte)0x9E; 
        //�ַ�����1
        cmd[0x6] = (byte)(n.length() + 0x1);
        //��7λ���������ÿ���ַ���hex��
        pos = 0x7;
        
        for(int i = 0x0; i < n.length(); i = i + 0x1) {
            cmd[pos] = (byte)n.charAt(i);
            pos = pos + 0x1;
        }
        cmd[pos] = 0x0;
        pos = pos + 0x1;
        cmd[pos] = (byte)((s.length() + 0x1) & 0xff);
        pos = pos + 0x1;
        cmd[pos] = (byte)((s.length() + 0x1) >> 0x8);
        pos = pos + 0x1;
        for(int i = 0x0; i < s.length(); i = i + 0x1) {
       
        cmd[pos] = (byte)s.charAt(i);
        pos = pos + 0x1;
        }
        cmd[pos] = 0x0;
        os.write(cmd);
    }
    
    public void send(String n, int nubmer) {
    	
    	//define cmd byte   n.length()+14 bytes 
        byte[] cmd = new byte[(n.length() + 0xe)];
        
        //initiate byte position
        int pos = 0x0;
        
        //stringsS
        //��¼�ַ���
        cmd[0] = (byte)((cmd.length - 0x2) & 0xff);
        //�ַ�������8λ����λ
        cmd[0x1] = (byte)((cmd.length - 0x2) >> 0x8);
        //00
        cmd[0x2] = 0x0;
        //00
        cmd[0x3] = 0x0;
        //81
        cmd[0x4] = (byte) 0x81;
        //9E
        cmd[0x5] = (byte)0x9E;
        //�ַ�����1
        cmd[0x6] = (byte)(n.length() + 0x1);
        //��7λ���������ÿ���ַ���hex��
        pos = 0x7;
        for(int i = 0x0; i < n.length(); i = i + 0x1) {
            cmd[pos] = (byte)n.charAt(i);
            pos = pos + 0x1;
        }
        
        //numberic
        //0
        cmd[pos] = 0x0;
        pos = pos + 0x1;
        //4
        cmd[pos] = 0x4;
        pos = pos + 0x1;
        //0
        cmd[pos] = 0x0;
        pos = pos + 0x1;
        //0
        cmd[pos] = 0x0;
        pos = pos + 0x1;
        //0
        cmd[pos] = 0x0;
        pos = pos + 1;
        //number to send
        switch(nubmer) {
            case 0:
            {
            	//00 00
                cmd[pos] = 0x0;
                pos = pos + 0x1;
                cmd[pos] = 0x0;
                os.write(cmd);
            
                
            }break;
            case 1:
            {
            	//80 3F
                cmd[pos] = (byte) 0x80;
                pos = pos + 1;
                cmd[pos] = 0x3f;
                os.write(cmd);
                
            }break;
            case 2:
            {
            	//00 40
                cmd[pos] = 0x0;
                pos = pos + 1;
                cmd[pos] = 0x40;
                os.write(cmd);
              
            }break;
            case 3:
            {
            	//40 40
                cmd[pos] = 0x40;
                pos = pos + 1;
                cmd[pos] = 0x40;
                os.write(cmd);
              
            }break;
            
            case 4:
            {
            	//80 40
                cmd[pos] = (byte) 0x80;
                pos = pos + 1;
                cmd[pos] = 0x40;
                os.write(cmd);
               
            }break;
            case 5:
            {
            	//A0 40
                cmd[pos] = (byte) 0xA0;
                pos = pos + 1;
                cmd[pos] = 0x40;
                os.write(cmd);
              
            }break;
            case 6:
            {
            	//C0 40
                cmd[pos] = (byte) 0xC0;
                pos = pos + 1;
                cmd[pos] = 0x40;
                os.write(cmd);
             
            }break;
            case 7:
            {
            	//E0 40
                cmd[pos] = (byte) 0xE0;
                pos = pos + 1;
                cmd[pos] = 0x40;
                os.write(cmd);
              
            }break;
            
            case 8:
            {
            	//00 41
                cmd[pos] = 0x00;
                pos = pos + 1;
                cmd[pos] = 0x41;
                os.write(cmd);
            
            }break;
            case 9:
            {
            	//10 41
                cmd[pos] = 0x10;
                pos = pos + 1;
                cmd[pos] = 0x41;
                os.write(cmd);
                
            }break;
            case 10:
            {
            	//20 41
                cmd[pos] = 0x20;
                pos = pos + 1;
                cmd[pos] = 0x41;
                os.write(cmd);
            
              
            }break;
           
            case 11:
            {
            	//20 41
                cmd[pos] = 0x30;
                pos = pos + 1;
                cmd[pos] = 0x41;
                os.write(cmd);
            
              
            }break;
            case 12:
            {
            	//20 41
                cmd[pos] = 0x40;
                pos = pos + 1;
                cmd[pos] = 0x41;
                os.write(cmd);
            
              
            }break;
            //���Ѿ�����ÿ�����ֵ�hex������һ���Ĺ��ɣ���ʵ�����õ��ƺ���������
            //���ǳ��ο�������뻹�Ǽ����˱ȽϺ����
        }
   
        }
    }
    
   
	
   



	


   