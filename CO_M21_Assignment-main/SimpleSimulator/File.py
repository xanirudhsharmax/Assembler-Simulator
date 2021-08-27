import matplotlib.pyplot as plt

Input = []
Dump = []


def memoryDump():
    for i in range(len(Input)):
        Dump.append(Input[i])
    i = len(Dump)
    while (i < 256):
        Dump.append('0000000000000000')
        i += 1


Reg = []
for i in range(0, 8):
    Reg.append('0000000000000000')

tem = Reg.copy()


def takeInput():
    while True:
        t = input()
        if not (t == '1001100000000000'):
            Input.append(t)
        else:
            Input.append(t)
            break


def decimalToBinary16(n):
    s = bin(n)
    s = s.replace('0b', '')
    while len(s) < 16:
        s = '0' + s
    return s


def decimalToBinary8(n):
    s = bin(n)
    s = s.replace('0b', '')
    while len(s) < 8:
        s = '0' + s
    return s


def register():
    reg = {"000": 'R0',
           "001": 'R1',
           "010": 'R2',
           "011": 'R3',
           "100": 'R4',
           "101": 'R5',
           "110": 'R6',
           "111": 'FLAGS'}


def binaryTODecimal(s):
    n = 0
    t = 1
    for i in s:
        i = int(i)
        n = n + pow(2, len(s) - t) * i
        t += 1
    return n


def opCode(s):  # passes instruction to updateReg
    op = {'00000': 'add', '00001': 'sub', '00110': 'mul', '01011': 'or', '01010': 'xor', '01100': 'and',
          '00010': 'mov', '01000': 'rs', '01001': 'ls',
          '00011': 'mov', '00111': 'div', '01101': 'not', '01110': 'cmp',
          '00100': 'ld', '00101': 'st', '01111': 'jmp', '10000': 'jlt', '10001': 'jgt',
          '10010': 'je', '10011': 'hlt'
          }

    for i in op:
        if (i == s):
            return op.get(i)


def findRegister(s):
    if (s == '000'):
        return 'R0'
    elif (s == '001'):
        return 'R1'
    elif (s == '010'):
        return 'R2'
    elif (s == '011'):
        return 'R3'
    elif (s == '100'):
        return 'R4'
    elif (s == '101'):
        return 'R5'
    elif (s == '110'):
        return 'R6'
    elif (s == '111'):
        return 'FLAGS'


def updateReg(i):
    s = opCode(i[0:5])
    ret = -1

    if (s == 'add' or s == 'sub' or s == 'mul' or s == 'or' or s == 'xor' or s == 'and'):
        s1 = findRegister(i[7:10])
        s2 = findRegister(i[10:13])
        s3 = findRegister(i[13:16])

        a, b, c = 0, 0, 0
        if (s1 == 'FLAGS'):
            a = 7
        else:
            a = int(s1[1])

        if (s2 == 'FLAGS'):
            b = 7
        else:
            b = int(s2[1])

        if (s3 == 'FLAGS'):
            c = 7
        else:
            c = int(s3[1])

        d2 = binaryTODecimal(tem[b])
        d3 = binaryTODecimal(tem[c])

        if (s == 'add'):
            if (d2 + d3 >= 0 and d2 + d3 <= 255):
                tem[a] = decimalToBinary16(d2 + d3)
            elif (d2 + d3 > 255 or d2 + d3 < 0):
                tem[a] = decimalToBinary16(d2 + d3)[8:16]
                tem[7] = tem[7][0:12] + '1' + tem[7][13:16]

        elif (s == 'sub'):
            if (d2 - d3 >= 0 and d2 - d3 <= 255):
                tem[a] = decimalToBinary16(d2 - d3)
            elif (d2 - d3 > 255 or d2 - d3 < 0):
                tem[int(s1[1])] = '00000000'
                tem[7] = tem[7][0:12] + '1' + tem[7][13:16]

        elif (s == 'mul'):
            if (d2 * d3 >= 0 and d2 * d3 <= 255):
                tem[a] = decimalToBinary16(d2 * d3)
            elif ():
                tem[a] = decimalToBinary16(d2 * d3)[8:16]
                tem[7] = tem[7][0:12] + '1' + tem[7][13:16]

        elif (s == 'xor'):
            tem[a] = decimalToBinary16(d2 ^ d3)

        elif (s == 'or'):
            tem[a] = decimalToBinary16(d2 | d3)

        elif (s == 'and'):
            tem[a] = decimalToBinary16(d2 & d3)


    elif ((s == 'rs' or s == 'ls' or i[0:5] == '00010')):  # B aur C dono me mov hai extra condition dene padege
        reg = findRegister(i[5:8])
        n = binaryTODecimal(i[8:16])

        if (reg == 'FLAGS'):
            r = 7
        else:
            r = int(reg[1])

        if (s == 'mov'):
            tem[r] = decimalToBinary16(n)
        elif (s == 'ls'):
            num = binaryTODecimal(tem[r])
            tem[r] = decimalToBinary16(num << n)
        elif (s == 'rs'):
            num = binaryTODecimal(tem[r])
            tem[r] = decimalToBinary16(num >> n)

    elif (s == 'mov' or s == 'div' or s == 'not' or s == 'cmp'):
        r1 = findRegister(i[10:13])
        r2 = findRegister(i[13:16])

        if (r1 == 'FLAGS'):
            a = 7
        else:
            a = int(r1[1])

        if (r2 == 'FLAGS'):
            b = 7
        else:
            b = int(r2[1])

        if (s == 'mov'):
            tem[a] = tem[b]
            tem[7] = '0000000000000000'
        elif (s == 'div'):
            d1 = binaryTODecimal(tem[a])
            d2 = binaryTODecimal(tem[b])

            tem[0] = decimalToBinary16(d1 // d2)
            tem[1] = decimalToBinary16(d1 % d2)
        elif (s == 'not'):
            num = binaryTODecimal(tem[b])
            tem[a] = decimalToBinary16(~num)
        elif (s == 'cmp'):
            d1 = binaryTODecimal(tem[a])
            d2 = binaryTODecimal(tem[b])
            if (d1 > d2):
                tem[7] = tem[7][0:14] + '1' + tem[7][15:16]
            elif (d1 < d2):
                tem[7] = tem[7][0:13] + '1' + tem[7][14:16]
            elif (d1 == d2):
                tem[7] = tem[7][0:15] + '1'

    elif (s == 'ld' or s == 'st'):
        r = findRegister(i[5:8])

        if (r == 'FLAGS'):
            a = 7
        else:
            a = int(r[1])

        mem = i[8:16]
        n = binaryTODecimal(mem)

        if (s == 'ld'):
            tem[a] = Dump[n]
        elif (s == 'st'):
            Dump[n] = tem[a]

    elif (s == 'jmp' or s == 'jlt' or s == 'jgt' or s == 'je'):

        mem = i[8:16]
        n = binaryTODecimal(mem)

        if (s == 'jmp'):
            ret = n

        elif (s == 'jlt'):
            if (tem[7][13:14] == '1'):
                ret = n

        elif (s == 'jgt'):
            if (tem[7][14:15] == '1'):
                ret = n

        elif (s == 'je'):
            if (tem[7][15:16] == '1'):
                ret = n

        tem[7] = '0000000000000000'

    elif (s == 'hlt'):
        print(end="")

    l = []
    l.append(tem)
    l.append(ret)

    return l


# def printReg():
#     a = 0
#     while(a<len(Input)):
#         S = decimalToBinary8(a)
#         print(S, end = " ")
#         tem = updateReg(Input[a])
#         for i in tem:
#             print(i ,end = " ")
#         print()
#         a+=1

def printReg():
    p_lst = []
    PC = 0
    while (PC < len(Input)):
        S = decimalToBinary8(PC)
        print(S, end=" ")
        lst = updateReg(Input[PC])
        arr = lst[0]
        for i in arr:
            print(i, end=" ")
        print()
        p_lst.append(PC)
        if (lst[1] == -1):
            PC += 1
        else:
            PC = lst[1]
    return p_lst


def graph(cylce, PC):
    plt.scatter(Cycle, PC)
    plt.xlabel("Cycle")
    plt.ylabel("Address")
    plt.title("Memory Address V/S Cycle")
    plt.savefig("pic.png")
    plt.show()


def printMem():
    for i in Dump:
        print(i)


takeInput()
memoryDump()
PC = printReg()
printMem()

Cycle = []
for i in range(len(Input)):
    Cycle.append(i)

graph(Cycle, PC)

# 0001000100000101
# 0010100100000011
# 1001100000000000


# 0001000100000100  mov r1 $4
# 0001001000000100  mov r2 $4
# 0111000000001010  cmp r1 r2
# 0001100000011111  mov r3 flags
# 0001010000000001  mov r4 $1
# 0111000000011100  cmp r3 r4
# 1000100000000111  jgt 7
# 1001100000000000  hlt