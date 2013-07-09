//  ************************************************************
//  *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
//  *                                                          *
//  *  See the file LICENCE.txt for copying permission.        *
//  ************************************************************ 

using System.IO;
using System.Runtime.Serialization.Formatters.Binary;

namespace ExcelAllGrade.util.serialization
{
    /// <summary>
    /// Provides a way to deep-clone an <see cref="System.Object"/> which implements <see cref="System.Runtime.Serialization.ISerializable"/>.
    /// </summary>
    public static class ObjectCloner
    {
        /// <summary>
        /// Deep-clones an <see cref="System.Object"/>.
        /// </summary>
        /// <param name="obj">The <see cref="System.Object"/> to deep-clone.</param>
        /// <returns>A cloned <paramref name="obj"/>.</returns>
        public static object DeepClone(object obj)
        {
            object objResult = null;
            using (MemoryStream ms = new MemoryStream())
            {
                BinaryFormatter bf = new BinaryFormatter();
                bf.Serialize(ms, obj);

                ms.Position = 0;
                objResult = bf.Deserialize(ms);
            }
            return objResult;
        }
    }
}
