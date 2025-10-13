﻿using lab_2;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab_2
{
    public class Knight : Hero
    {
        public Knight() : base("Knight", 150, 30, 10)
        {

        }

        public override void ApplySpecialAbility(params Hero[] hero)
        {
            foreach (var h in hero)
            {
                h.Attack(h);
            }
        }
    }
}