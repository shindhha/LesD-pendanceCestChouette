#include "init_uc2.h"
#include "c8051F12.h"
#include "types.h"
#include "uart1.h"

#define NB_SWITCHS 26
#define DEBUT_MSG 0x02
#define DEMANDE_ETAT_LEDS 0x04

// Interruption timer
u8sod u8_top_tb;


u8sod u8_lu;

/* D�claration des switchs */
sbit S1_2 = P2^0;
sbit S1_1 = P2^1;
sbit S2_2 = P2^6;
sbit S2_1 = P2^7;
sbit S3_2 = P2^4;
sbit S3_1 = P2^5;
sbit S4_2 = P2^5; // A changer
sbit S4_1 = P2^5; // A changer
sbit S5_2 = P5^2;
sbit S5_1 = P5^3;
sbit S6_2 = P5^4;
sbit S6_1 = P3^6;
sbit S7_2 = P2^2;
sbit S7_1 = P2^3;
sbit S8_2 = P3^0;
sbit S8_1 = P3^1;
sbit S9_2 = P3^2;
sbit S9_1 = P3^3;
sbit S10_2 = P3^4;
sbit S10_1 = P3^5;
sbit ROT1_1 = P4^0;
sbit ROT1_2 = P4^1;
sbit ROT1_3 = P4^2;
sbit ROT2_1 = P4^3;
sbit ROT2_2 = P4^4;
sbit ROT2_3 = P4^5;

/* D�claration des leds */
sbit LED_D1 = P5^0;
sbit LED_D4 = P5^1;

//sbit switchs[NB_SWITCHS] = {
//	S1_1, S1_2, S2_1, S2_2, S3_1, S3_2, S4_1, S4_2, 
//	S5_1, S5_2, S6_1, S6_2, S7_1, S7_2, S8_1, S8_2,
//	S9_1, S9_2, S10_1, S10_2, ROT1_1, ROT1_2, ROT1_3,
//	ROT2_1, ROT2_2, ROT2_3
//};
// Impossible de cr�er un tableau de sbit dans la version actuelle
// On va donc utiliser un switch case


void choisir_switch(u8sod num_switch, bit on);
void maj_switchs();
void envoyer_etat_leds();


void choisir_switch(u8sod num_switch, bit on)
{
	switch (num_switch)
	{
		case 0:
			S1_1 = on;
			break;
		case 1:
			S1_2 = on;
			break;
		case 2:
			S2_1 = on;
			break;
		case 3:
			S2_2 = on;
			break;
		case 4:
			S3_1 = on;
			break;
		case 5:
			S3_2 = on;
			break;
		case 6:
			S4_1 = on;
			break;
		case 7:
			S4_2 = on;
			break;
		case 8:
			S5_1 = on;
			break;
		case 9:
			S5_2 = on;
			break;
		case 10:
			S6_1 = on;
			break;
		case 11:
			S6_2 = on;
			break;
		case 12:
			S7_1 = on;
			break;
		case 13:
			S7_2 = on;
			break;
		case 14:
			S8_1 = on;
			break;
		case 15:
			S8_2 = on;
			break;
		case 16:
			S9_1 = on;
			break;
		case 17:
			S9_2 = on;
			break;
		case 18:
			S10_1 = on;
			break;
		case 19:
			S10_2 = on;
			break;
		case 20:
			ROT1_1 = on;
			break;
		case 21:
			if (ROT1_1 == 0) {
				ROT1_2 = on;
			}
			break;
		case 22:
			if (ROT1_1 == 0) {
				ROT1_3 = on;
			}
			break;
		case 23:
			ROT2_1 = on;
			break;
		case 24:
			if (ROT2_1 == 0) {
				ROT2_2 = on;
			}
			break;
		case 25:
			if (ROT2_1 == 0) {
				ROT2_3 = on;
			}
			break;
	}
}

/*
 * Les messages re�us sont de la forme :
 *
 *    0x02	          			[1 octet data]
 * D�but de comm     11 [5 bit num switch] [1 bit on/off]
 */
void maj_switchs() 
{
	u8sod num_switch;
	bit on;
	while (1)
	{
		if (u8NbOctetsDansFifoRxDialCPU() > 0)
		{
			u8_lu = u8DepileUnOctetDeFifoRxDialCPU();
			on = (u8_lu & 0x01) == 1;
			num_switch = (u8_lu & 0x3E) >> 1;
			if (num_switch < NB_SWITCHS)
			{
				choisir_switch(num_switch, on);
			}
			break;
		}
	}
}

/*
 * Envoie un octet contenant l'�tat des leds D1 et D4
 * Octet de forme : 000000[D4][D1]
 * avec D1 et D4 = 0 ou 1 en fonction de l'�tat
 * D4 = 0 -> D4 allum�e
 * D1 = 0 -> D1 �teinte
 */
void envoyer_etat_leds()
{
	u8sod D1 = LED_D1 & 0x01;
	u8sod D4 = ~LED_D4 & 0x01;
	u8sod etat_leds = D1 | (D4 << 1);
	u8EcritDansFifoTxDialCPU(etat_leds);
	Ecriture_Statut_Emission_Uart1(EMISSION_UART1_EN_COURS);
}

void main() {

	//Init_micro_interne();
	Init_Device();

	Init_Uart1();

	//autorisation interruption timer0
	IE=IE | 0x02;

	//autorisation it UART1
	Autorise_Interruption_Uart1();

	//autorisation des interruptions
	IE=IE | 0x80;

	while (1) {
		if (u8_top_tb==1)
		{	
			u8_top_tb =0;
			if (u8NbOctetsDansFifoRxDialCPU() > 0)
			{
				u8_lu = u8DepileUnOctetDeFifoRxDialCPU();
				/* Mise a jout des switchs */
				if (u8_lu == DEBUT_MSG)
				{
					maj_switchs();
				}
				/* Demande de l'�tat des leds */
				else if (u8_lu == DEMANDE_ETAT_LEDS)
				{
					envoyer_etat_leds();
					Ecriture_Statut_Emission_Uart1(EMISSION_UART1_TERMINEE);
				}
			}

		}
	}
	
}