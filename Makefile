CFLAGS = -O2 -Wall
HIDAPI = hidraw
LDFLAGS = -lhidapi-$(HIDAPI) -I/usr/local/Cellar/hidapi/0.8.0-rc1/include/ -L/usr/local/Cellar/hidapi/0.8.0-rc1/lib

all: usbrelay

clean:
	rm -f usbrelay

install: usbrelay
	install -d $(DESTDIR)/usr/bin
	install -m 0755 usbrelay $(DESTDIR)/usr/bin

.PHONY: all clean install
