# -*- Mode: Python; coding: utf-8; indent-tabs-mpythoode: nil; tab-width: 4 -*-

'''
Objetivo:
	Gerar um arquivo CSV para uso de questões no QUIZ Java.

	Arquivo base:
	<# Marcador de início de line
	#> Marcador de fim de line

	Linhas em branco são ignoradas
'''	

import os, sys

INPUT = ""
OUTPUT = "../questions/"
TIPO = ".csv"
DELIM_W = ';'	# output

def read_file(file):
	list_txt = []
	try:
		f = open(file)
		for line in f:
			list_txt += [line.replace("\n", "")]
		f.close()
	except Exception:
		pass

	return list_txt

def save_file(file, txt_input):
	try:
		file = open(file, "w")
		file.write(txt_input)
		file.close()
	except Exception:
		print("error " + file)
		exit(0)	

def convert(list_txt):
	txt_output = ""
	question = ""
	txt_valid = False
	for info in list_txt:
		if (info[:2]=="<#"):
			txt_valid = True
			print(info[2:])
			continue
		if (info[:2]=="#>" and txt_valid):
			txt_valid = False
			txt_output = txt_output + question + "\n;;;;;;;\n"
			question = ""
		if (info == ""):
			continue
		if (txt_valid):
			question = question + info + DELIM_W
	return txt_output

def main():
	# arquivo de entrada
	filename_input = "questions-visitante_base.txt"
	file_input = INPUT + filename_input

	# arquivo de saída
	filename = "questions_visitante"
	file_output = OUTPUT + filename + TIPO

	# ler arquivo com questões sem formatação
	txt_input = read_file(file_input)
	txt_output = convert(txt_input)
	# print("\n" + txt_output)

	# salvar list_txt gerada
	print ("save " + file_output)
	save_file(file_output, txt_output)

if __name__ == '__main__':
	main()