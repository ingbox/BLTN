import random
from pydub import AudioSegment

# 초성 리스트. 00 ~ 18
CHOSUNG_LIST = ['ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ']
# 중성 리스트. 00 ~ 20
JUNGSUNG_LIST = ['ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ']
# 종성 리스트. 00 ~ 27 + 1(1개 없음)
JONGSUNG_LIST = [' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ']

def korean_animalize(stringy, pitch):

	sounds = {}

	keys = ['ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ','ㅏ', 'ㅐ',
			'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ',
			'*ㄱ', '*ㄲ', 'ㄳ', '*ㄴ', 'ㄵ', 'ㄶ', '*ㄷ', '*ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', '*ㅁ', '*ㅂ', 'ㅄ', '*ㅅ', '*ㅆ', '*ㅇ', '*ㅈ', '*ㅊ', '*ㅋ', '*ㅌ', '*ㅍ', '*ㅎ',' ', '.']

	for index, ltr in enumerate(keys):
		num = index+1
		if num < 10:
			num = '0'+str(num)
		sounds[ltr] = './sounds/'+pitch+'/sound'+str(num)+'.wav'

	if pitch == 'med':
		rnd_factor = .45
	else:
		rnd_factor = .25

	infiles = []

	for i, char in enumerate(stringy):
		try:
			if (char == 'ㄱ' or char == 'ㄲ' or char == 'ㄴ' or char == 'ㄷ'
				or char == 'ㄹ' or char == 'ㅁ' or char == 'ㅂ' or char == 'ㅅ' or char == 'ㅆ'or char == 'ㅇ' or char == 'ㅈ' or char == 'ㅊ'
				or char == 'ㅋ' or char == 'ㅌ' or char == 'ㅍ' or char == 'ㅎ') and (stringy[i+1] == 'ㄱ' or stringy[i+1] == 'ㄲ'
																				  or stringy[i+1] == 'ㄴ' or stringy[i+1] == 'ㄷ' or stringy[i+1] == 'ㄹ' or stringy[i+1] == 'ㅁ' or stringy[i+1] == 'ㅂ' or stringy[i+1] == 'ㅅ'
																				  or stringy[i+1] == 'ㅆ' or stringy[i+1] == 'ㅇ' or stringy[i+1] == 'ㅈ' or stringy[i+1] == 'ㅊ' or stringy[i+1] == 'ㅋ' or stringy[i+1] == 'ㅌ'
																				  or stringy[i+1] == 'ㅍ' or stringy[i+1] == 'ㅎ' or stringy[i+1] == ' '): #test for 'sh' sound
				print("counting")
				infiles.append(sounds['*'+char])
				continue
			elif char == ',' or char == '?':
				infiles.append(sounds['.'])
				continue
			else:
				print(sounds[char])
				infiles.append(sounds[char])

		except:
			pass

		# if not char.isalpha() and char != '.': # skip characters that are not letters or periods.
		# 	continue
		# infiles.append(sounds[char])

	combined_sounds = None

	print(len(infiles))
	for index,sound in enumerate(infiles):
		tempsound = AudioSegment.from_wav(sound)
		if stringy[len(stringy)-1] == '?':
			if index >= len(infiles)*.8:
				octaves = random.random() * rnd_factor + (index-index*.8) * .1 + 0.4 # shift the pitch up by half an octave (speed will increase proportionally)
			else:
				octaves = random.random() * rnd_factor + 2.0
		else:
			octaves = random.random() * rnd_factor + 1.0 # shift the pitch up by half an octave (speed will increase proportionally)
		new_sample_rate = int(tempsound.frame_rate * (2.0 ** octaves))
		new_sound = tempsound._spawn(tempsound.raw_data, overrides={'frame_rate': new_sample_rate})
		new_sound = new_sound.set_frame_rate(44100) # set uniform sample rate
		combined_sounds = new_sound if combined_sounds is None else combined_sounds + new_sound


	combined_sounds.export("hello.wav", format="wav")

def korean_decode(korean_word):
    r_lst = []
    for w in list(korean_word.strip()):
        ## 영어인 경우 구분해서 작성함.
        if '가' <= w <= '힣':
            ## 588개 마다 초성이 바뀜.

            ch1 = (ord(w) - ord('가')) // 588
            ## 중성은 총 28가지 종류

            ch2 = ((ord(w) - ord('가')) - (588 * ch1)) // 28
            ch3 = (ord(w) - ord('가')) - (588 * ch1) - 28 * ch2
            r_lst.append([CHOSUNG_LIST[ch1], JUNGSUNG_LIST[ch2], JONGSUNG_LIST[ch3]])
        else:
            r_lst.append([w])

    r_lst = sum(r_lst, [])
    sentence = "".join([str(_) for _ in r_lst])

    return sentence


if __name__ == '__main__':
	pitch = 'korean'
	stringy = korean_decode("안녕하세요 당신의 이름은 무엇입니까")
	korean_animalize(stringy, pitch)
