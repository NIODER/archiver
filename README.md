# archiver
RLE archiver university task
надо дописать короче метаданные, они неправильно кодируют "name", что есть имя файла
эти метаданные представляют из себя такую хуйню:
[размер файла(сжатого)] 8 байт
[размер имени(от 4 до 260 символов)] - 1 байт
[полное имя] - до 255 байт
так что надо это все перелопатить как-нибудь потом
